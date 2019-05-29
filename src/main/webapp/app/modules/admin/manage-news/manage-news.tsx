import '../admin.scss';
import React from 'react';
import { connect } from 'react-redux';
import ArrowBack from '@material-ui/icons/ArrowBack';
import Dialog from '@material-ui/core/Dialog';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import ToggleDisplay from 'react-toggle-display';
import { Typography } from '@material-ui/core';
import { Tabs, TabPanel, Tab, Choice } from '@cmsgov/design-system-core';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import MUIDataTable from 'mui-datatables';
import { Card, Row, Col } from 'reactstrap';
import { enableRequestButton, enableManageRequestButton } from '../../../shared/layout/header/nav-bar.reducer';
import * as RequestService from './manage-news-service';
import http from '../../../shared/service/http-service';
import _ from 'lodash';
import LoadingOverlay from 'react-loading-overlay';
import { FadeLoader } from 'react-spinners';

const archive = 'ARCHIVE';
const active = 'ACTIVE';
const activeSml = 'Active';
const archiveSml = 'Archive';
const published = 'published';
const edited = 'edited';
const move2Active = 'moved to active';
const move2Archive = 'moved to archive';
const strArchive = 'archive';
const strActive = 'active';
const today = new Date();
const dd = today.getDate();
let ddStr = String(dd);
if (dd < 10) ddStr = 0 + ddStr;
const mm = today.getMonth() + 1;
let mmStr = String(mm);
if (mm < 10) mmStr = 0 + mmStr;
const yyyy = today.getFullYear();
const currentDate = mmStr + '/' + ddStr + ' /' + yyyy;
const secondaryColor = '#046B99';
const theme = createMuiTheme({
  overrides: {
    MuiToolbar: {
      root: {
        marginBottom: '25px'
      }
    },
    MuiTableHead: {
      root: {
        fontWeight: 'bold',
        fontSize: '14px',
        color: '#000'
      }
    },
    MuiTableCell: {
      body: {
        fontSize: '0.95rem'
      },
      head: {
        fontWeight: 'bold',
        fontSize: '13.5px',
        color: '#000'
      }
    },
    MuiSvgIcon: {
      root: {
        color: `${secondaryColor}`
      }
    },
    MuiChip: {
      root: {
        color: `${secondaryColor}`
      }
    }
  },
  typography: {
    useNextVariants: true
  }
});
export class ManageNews extends React.Component<DispatchProps> {
  emptyNews = { desc: '', postDate: '', active: 'O' };
  state = {
    requests: [],
    news: [],
    addNews: { id: null, active: 'O', postDate: '', desc: '' },
    enableEdit: true,
    showAddNews: false,
    back: false,
    showManageNews: true,
    rowMeta: { rowIndex: 0, rowNumber: 0 },
    submittedRequestCounter: RequestService.data.length,
    rowData: [],
    newsEvent: '',
    postedOn: '',
    newsStatus: activeSml,
    newsIdNumber: null,
    isChecked: true,
    isEdit: false,
    disableAdd: false,
    radioValue: '',
    editData: [],
    selectedIds: '',
    prevSelectedId: '',
    forSubmittedTab: true,
    draftRequestCounter: 0,
    activeNewsArray: [],
    archiveNewsArray: [],
    dataArrayActive: [],
    dataArrayArchive: [],
    showPreviewNews: false,
    idNum: 100,
    showAddNewsNotifcation: false,
    alertOpen: false,
    alertText: '',
    confirmationMessageType: '',
    alertTextSmall: '',
    controllCheckedStatus: [],
    postNews: {},
    loading: true,
    newsId: '',
    showErrorMessage: false,
    errorMessage: ''
  };

  componentDidMount() {
    this.getData();
    this.setState({ rowData: this.state.news });
    this.props.enableManageRequestButton();
    this.props.enableRequestButton();
    this.setState({ rowData: this.state.news });
  }
  getData = () => {
    http
      .get(`api/get-all-news`)
      .then(response => {
        this.setState({ news: response.data, loading: false, showAddNewsNotifcation: false });
      })
      .catch(error => {
        this.setState({ news: [], loading: false });
        console.error('error api call to api/get-all-news ' + error);
      });
  };
  postData = () => {
    const { postNews, isEdit } = this.state;
    const endpoint = isEdit ? 'api/update-news' : 'api/add-news';
    if (isEdit) {
      http
        .put(endpoint, postNews)
        .then(response => {
          this.setState({ newsId: response.data, loading: false, showAddNewsNotifcation: true, showManageNews: true });
          this.state.isEdit
            ? this.setState({
                news: [this.state.addNews, ..._.filter(this.state.news, item => item.id !== this.state.rowData[1])],
                isEdit: false,
                showPreviewNews: false,
                isChecked: false,
                confirmationMessageType: edited
              })
            : this.setState({ news: [this.state.addNews, ...this.state.news], confirmationMessageType: published });
          this.setState({ showAddNews: false, showPreviewNews: false, isEdit: false, isChecked: false });
          this.getData();
        })
        .catch(error => {
          this.setState({ loading: false, showErrorMessage: true });
          console.error('error api call ' + endpoint + error);
        });
    } else {
      http
        .post(endpoint, postNews)
        .then(response => {
          this.setState({ newsId: response.data, loading: false, showAddNewsNotifcation: true, showManageNews: true });
          this.state.isEdit
            ? this.setState({
                news: [this.state.addNews, ..._.filter(this.state.news, item => item.id !== this.state.rowData[1])],
                isEdit: false,
                showPreviewNews: false,
                isChecked: false,
                confirmationMessageType: edited
              })
            : this.setState({ news: [this.state.addNews, ...this.state.news], confirmationMessageType: published });
          this.setState({ showAddNews: false, showPreviewNews: false, isEdit: false, isChecked: false });
          this.getData();
        })
        .catch(error => {
          const errorDes = error.response !== undefined ? error.response.data.errors[0] : error;
          const errorMessage =
            error.response !== undefined
              ? 'The text you entered ' + ' " ' + errorDes.rejectedValue + ' " ' + errorDes.defaultMessage + ' .'
              : error.message;
          this.setState({
            loading: false,
            showErrorMessage: true,
            errorMessage
          });
          console.error('error api call to ' + endpoint + ' ' + errorMessage);
        });
    }
  };

  handleYes = value => {
    if (value === activeSml) {
      this.handleMoveToActive();
    }
    if (value === archiveSml) {
      this.handleMoveToArchive();
    }
  };
  editButtonHandler = () => {
    this.setState({ showErrorMessage: false });
    this.setState({ addNews: { id: null } });
    this.setState({ isEdit: true });
    this.setState({ showAddNewsNotifcation: false });
    this.setState({ enableEdit: false });
    this.setState({ disableAdd: true });
    this.setState({ newsStatus: this.state.rowData[1] });
    this.setState({ idNum: this.state.rowData[0].props.children.props.children.props.children[1].props.htmlFor });
    this.state.rowData[2] === archiveSml ? this.setState({ isChecked: false }) : this.setState({ isChecked: true });
  };
  handleBackButton = () => {
    this.setState({
      back: true,
      showAddNews: false,
      showManageNews: true,
      isEdit: false,
      showPreviewNews: false,
      showErrorMessage: false
    });
  };
  handleAddNews = () => {
    this.setState({ showAddNews: true });
    this.setState({ showManageNews: false });
    this.setState({ showPreviewNews: false });
  };
  handleEditNews = () => {
    this.setState({ showAddNews: true, isEdit: true, showPreviewNews: false });
    this.setState({
      newsEvent: this.state.rowData[3],
      newsStatus: true,
      idNum: this.state.rowData[1],
      newsIdNumber: this.state.rowData[1]
    });
    this.setState({ showManageNews: false });
    this.handleRadioCheckedStatus();
  };
  handleNewsPublish = () => {
    this.setState({ loading: true });
    this.postData();
  };
  editNewsHandler = () => {
    this.setState({ showAddNews: false, showPreviewNews: false, errorMessage: '' });
    this.setState({ showManageNews: true });
    this.setState({
      addNews: {
        id: this.state.newsIdNumber,
        active: 'O',
        postDate: currentDate,
        desc: this.state.newsEvent
      }
    });
    const index = this.state.rowMeta.rowIndex;
    this.setState({ news: [this.state.addNews, ...this.state.news.filter(i => i !== this.state.news[index])] });
  };
  handleNewsEvent = e => {
    this.setState({ newsEvent: e.currentTarget.value });
  };
  handleArchiveValue = () => {
    this.setState({ newsStatus: archiveSml });
    this.setState({ isChecked: false });
  };
  handleActiveValue = () => {
    this.setState({ newsStatus: activeSml });
    this.setState({ isChecked: true });
  };
  handleNewsPreview = () => {
    console.log(this.state.rowData[1]);
    this.setState({ showPreviewNews: true, showAddNews: false, showErrorMessage: false, loading: false });
    this.state.isEdit
      ? this.setState({
          postNews: {
            id: this.state.rowData[1],
            active: 'O',
            postDate: this.state.rowData[2],
            desc: this.state.newsEvent
          }
        })
      : this.setState({
          postNews: {
            active: 'O',
            postDate: new Date(),
            desc: this.state.newsEvent
          }
        });
  };
  clearEvent = () => {
    this.setState({ newsEvent: '' });
  };
  handleMoveToActive = () => {
    const moveToActive = { id: null, active: null, postDate: '', desc: '' };
    moveToActive.id = this.state.rowData[1];
    moveToActive.active = true;
    moveToActive.postDate = this.state.rowData[2];
    moveToActive.desc = this.state.rowData[3];
    this.setState({ news: { status: true } });
    this.setState({ news: [moveToActive, ..._.filter(this.state.news, item => item.id !== moveToActive.id)] });
    this.setState({ alertOpen: false });
    this.setState({ showAddNewsNotifcation: true, confirmationMessageType: move2Active });
    this.handleRadioCheckedStatus();
  };
  handleRadioCheckedStatus = () => {
    let checkedStatus = [];
    for (const val of this.state.news) {
      const addFalse = this.state.controllCheckedStatus.push(false);
      checkedStatus = [...this.state.controllCheckedStatus, addFalse];
    }
    this.setState({ controllCheckedStatus: checkedStatus });
    setTimeout(() => {
      this.setState({ controllCheckedStatus: [] });
      this.setState({ enableEdit: true });
      this.setState({ disableAdd: false });
    }, 100);
  };
  handleOpenAlertMoveToArchive = () => {
    this.setState({ alertOpen: true });
    this.setState({ alertText: archiveSml });
    this.setState({ alertTextSmall: strArchive });
  };
  handleOpenAlertMoveToActive = () => {
    this.setState({ alertOpen: true });
    this.setState({ alertText: activeSml });
    this.setState({ alertTextSmall: strActive });
  };
  handleMoveToArchive = () => {
    const moveToArchive = { id: null, active: null, postDate: '', desc: '' };
    moveToArchive.id = this.state.rowData[1];
    moveToArchive.active = false;
    moveToArchive.postDate = this.state.rowData[2];
    moveToArchive.desc = this.state.rowData[3];
    this.setState({ news: { status: false } });
    this.setState({ news: [moveToArchive, ..._.filter(this.state.news, item => item.id !== moveToArchive.id)] });
    this.setState({ alertOpen: false });
    this.setState({ showAddNewsNotifcation: true, confirmationMessageType: move2Archive });
    this.handleRadioCheckedStatus();
  };
  handleClose = () => {
    this.setState({ alertText: '' });
    this.setState({ toNewRequestLanding: true });
    this.setState({ alertOpen: false });
    this.setState({ buttonDisabled: true });
  };
  render() {
    const { newsId, loading } = this.state;
    const columns = [
      {
        name: 'Select',
        options: {
          filter: false,
          search: false,
          sort: true,
          customBodyRender: value => {
            // tslint:disable-next-line:ter-arrow-body-style
            return (
              <React.Fragment>
                <div className="ds-u-float--left ">
                  <fieldset className="ds-c-fieldset">
                    <Choice
                      id={this.state.rowData[0]}
                      name="select"
                      type="radio"
                      checked={this.state.controllCheckedStatus[this.state.rowMeta.rowIndex]}
                      value={this.state.rowData[0]}
                      onClick={this.editButtonHandler}
                    />
                    <label htmlFor={this.state.rowData[0]}>{''}</label>
                  </fieldset>
                </div>
              </React.Fragment>
            );
          }
        }
      },
      'News Id',
      'Posted On',
      'Description'
    ]; // 'Status',
    const options = {
      filter: true,
      download: false,
      viewColumns: true,
      search: true,
      selectableRows: false,
      responsive: 'scroll',
      print: false,
      pagination: true,
      elevation: 10,
      rowsPerPage: 4,
      rowsPerPageOptions: [5, 10, 15],
      rowHover: true,
      selectable: true,
      textLabels: { body: { noMatch: 'There is no news item.' } },
      onRowClick: (rowData, rowMeta) => {
        this.setState({ rowData });
        this.setState({ checkedRadio: this.state.rowMeta.rowIndex });
        this.setState({ rowMeta });
      }
    };
    const { news } = this.state;

    const actNew = _.filter(news, item => item.active === true);
    const actArc = _.filter(news, item => item.active === false);

    const dataArrayActive = actNew.reduce<any[]>((all, data) => {
      all.push([data.id, data.id, data.postDate, data.desc]);
      return all;
    }, []);
    const dataArrayArchive = actArc.reduce<any[]>((all, data) => {
      all.push([data.id, data.id, data.postDate, data.desc]);
      return all;
    }, []);

    return (
      <React.Fragment>
        <Dialog maxWidth="sm" open={this.state.alertOpen} onClose={this.handleClose} aria-labelledby="dialog-title">
          <div className="ds-c-dialog-wrap" aria-labelledby="dialog-title" role="dialog">
            <div className="ds-c-dialog" role="document">
              <header className="ds-c-dialog__header" role="banner">
                <h1 className="ds-h2" id="dialog-title">
                  {this.state.alertText + ''} request
                </h1>

                <Grid id="dialogClose" item xs={2} className="pl-5 ml-5 ">
                  <button
                    className="ds-c-button ds-c-button--transparent ds-c-dialog__close pl-9 ml-5"
                    aria-label="Close modal dialog"
                    onClick={this.handleClose}
                  >
                    Close
                  </button>
                </Grid>
              </header>
              <main role="main">
                <p className="ds-text">
                  Are you sure you want to move this request to {''}
                  {this.state.alertTextSmall}?
                </p>
              </main>
              <aside className="ds-c-dialog__actions" role="complementary">
                <button
                  className="ds-c-button ds-c-button--success"
                  value={this.state.alertText}
                  onClick={() => {
                    this.handleYes(this.state.alertText);
                  }}
                >
                  Yes
                </button>

                <span id="dialogClose" className="pl-5">
                  <button
                    className="ds-c-button ds-c-button--transparent ds-c-dialog__close mt-0"
                    aria-label="Close modal dialog"
                    onClick={this.handleClose}
                  >
                    Cancel
                  </button>
                </span>
              </aside>
            </div>
          </div>
        </Dialog>
        <ToggleDisplay show={this.state.showAddNews}>
          <div className="mb-4" style={{ display: 'inline-block' }}>
            <Typography variant="h6" style={{ color: '#205493', cursor: 'pointer' }} onClick={this.handleBackButton}>
              <ArrowBack /> <u>Back</u>
            </Typography>
          </div>

          <Paper className="pt-4 pb-4" style={{ width: '80%' }}>
            <div className="pr-4 pl-4">
              <div className="add-news-item ">Add News Item</div>
              <fieldset className="ds-c-fieldset" style={{ paddingLeft: '70px' }}>
                <legend className="ds-c-label">News Status</legend>
                <input
                  className="ds-c-choice ds-c-choice--inverse"
                  id="active"
                  type="radio"
                  name="news-status"
                  value={this.state.newsStatus}
                  checked={this.state.isChecked}
                  onChange={this.handleActiveValue}
                />
                <label htmlFor="active">Active</label>
                <input
                  className="ds-c-choice ds-c-choice--inverse"
                  id="archive"
                  type="radio"
                  disabled
                  name="news-status"
                  checked={!this.state.isChecked}
                  value={this.state.newsStatus}
                  onChange={this.handleArchiveValue}
                />
                <label htmlFor="archive">Archive</label>
                <label className="ds-c-label" htmlFor="news-event">
                  News Event {'(*)'}
                  {500 - this.state.newsEvent.length}
                  {''} characters remaining
                </label>
                <textarea
                  rows={40}
                  cols={500}
                  maxLength={500}
                  className="ds-c-field"
                  id="news-event"
                  name="news-event"
                  value={this.state.newsEvent}
                  onChange={this.handleNewsEvent}
                />
                <button
                  className="ds-c-button ds-c-button--primary"
                  disabled={this.state.newsEvent === '' ? true : false}
                  onClick={this.handleNewsPreview}
                >
                  Preview
                </button>
                <a
                  style={{ color: '#0071bb', paddingLeft: '40px', fontSize: '19px', textDecoration: 'underline' }}
                  onClick={this.clearEvent}
                >
                  Clear
                </a>
              </fieldset>
            </div>
          </Paper>
        </ToggleDisplay>

        <ToggleDisplay show={this.state.showPreviewNews}>
          <div className="mb-4" style={{ display: 'inline-block' }}>
            <Typography variant="h6" style={{ color: '#205493', cursor: 'pointer' }} onClick={this.handleBackButton}>
              <ArrowBack /> <u>Back</u>
            </Typography>
          </div>

          <Paper className="pt-4 pb-4" style={{ width: '80%' }}>
            <ToggleDisplay show={this.state.showErrorMessage}>
              <div className="ds-c-alert ds-c-alert--error mb-3">
                <div className="ds-c-alert__body">
                  <p className="ds-c-alert__text">{this.state.errorMessage}</p>
                </div>
              </div>
            </ToggleDisplay>
            <LoadingOverlay
              active={loading}
              styles={{ overlay: base => ({ ...base, background: 'rgba(0, 0, 0, 0.1)' }) }}
              spinner={<FadeLoader color={'#4A90E2'} />}
            >
              <div className="pr-4 pl-4">
                <div className="add-news-item ">Preview News Item</div>
                <fieldset className="ds-c-fieldset" style={{ paddingLeft: '70px', paddingTop: '30px' }}>
                  <label>{this.state.isChecked ? 'Active' : 'Archive'}</label>
                  <label>{this.state.newsEvent}</label>
                  <div style={{ paddingTop: '50px' }}>
                    <button className="ds-c-button ds-c-button--primary" onClick={this.handleNewsPublish}>
                      Publish
                    </button>
                    <a
                      style={{ color: '#0071bb', paddingLeft: '40px', fontSize: '19px', textDecoration: 'underline' }}
                      onClick={this.handleAddNews}
                    >
                      Edit
                    </a>
                  </div>
                </fieldset>
              </div>
            </LoadingOverlay>
          </Paper>
        </ToggleDisplay>

        <ToggleDisplay show={this.state.showManageNews}>
          <Row>
            <Col xs="8" sm="11" className="col-manage-request">
              <Card body>
                <div className="manage-news">Manage News</div>

                <ToggleDisplay show={this.state.showAddNewsNotifcation}>
                  <div className="ds-c-alert ds-c-alert--success">
                    <div className="ds-c-alert__body">
                      <p className="ds-c-alert__text">
                        News ID# {''} {newsId} {''} has been {this.state.confirmationMessageType}
                      </p>
                    </div>
                  </div>
                </ToggleDisplay>

                <Tabs
                  defaultSelectedId="active-news"
                  onChange={(selectedId, prevSelectedId) => {
                    this.setState({ selectedIds: prevSelectedId });
                    this.setState({ forSubmittedTab: !this.state.forSubmittedTab });
                  }}
                >
                  <TabPanel
                    id="archive"
                    tab={
                      <div className="archive-news-admin">
                        {archive + ' ' /*'(' + this.state.draftRequestCounter + ')'*/}
                        {<span className="ds-c-badge">{dataArrayArchive.length}</span>}
                      </div>
                    }
                  >
                    <Row>
                      <Col className="text-right">
                        {/*<button
                          value="active"
                          disabled={this.state.enableEdit}
                          onClick={this.handleOpenAlertMoveToActive}
                          className="ds-c-button ds-c-button--primary"
                        >
                          Move to active
                        </button>*/}
                      </Col>
                    </Row>
                    <MuiThemeProvider theme={theme}>
                      <MUIDataTable title="" data={dataArrayArchive} columns={columns} options={options} />
                    </MuiThemeProvider>
                  </TabPanel>
                  <TabPanel
                    id="active-news"
                    tab={
                      <div className="active-news-admin">
                        {active + ' ' /* + '(' + this.state.submittedRequestCounter + ')'*/}
                        {<span className="ds-c-badge">{dataArrayActive.length}</span>}
                      </div>
                    }
                  >
                    <Row>
                      <Col className="text-right">
                        <button className="ds-c-button ds-c-button--primary" disabled={this.state.disableAdd} onClick={this.handleAddNews}>
                          Add
                        </button>
                        <button
                          value="value"
                          disabled={this.state.enableEdit}
                          className="ds-c-button ds-c-button--primary"
                          onClick={this.handleEditNews}
                        >
                          Edit
                        </button>
                        {/*<button
                          value="archive"
                          disabled={this.state.enableEdit}
                          onClick={this.handleOpenAlertMoveToArchive}
                          className="ds-c-button ds-c-button--primary"
                        >
                          Move to archive
                        </button>*/}
                      </Col>
                    </Row>
                    <LoadingOverlay
                      active={loading}
                      styles={{ overlay: base => ({ ...base, background: 'rgba(0, 0, 0, 0.3)' }) }}
                      spinner={<FadeLoader color={'#4A90E2'} />}
                    >
                      <MuiThemeProvider theme={theme}>
                        <MUIDataTable title="" data={dataArrayActive} columns={columns} options={options} />
                      </MuiThemeProvider>
                    </LoadingOverlay>
                  </TabPanel>
                </Tabs>
              </Card>
            </Col>
          </Row>
        </ToggleDisplay>
      </React.Fragment>
    );
  }
}

const mapDispatchToProps = { enableManageRequestButton, enableRequestButton };

type DispatchProps = typeof mapDispatchToProps;

export default connect(
  null,
  mapDispatchToProps
)(ManageNews);
