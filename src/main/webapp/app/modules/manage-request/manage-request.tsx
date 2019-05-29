import './manage.scss';
import React from 'react';
import { connect } from 'react-redux';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import MUIDataTable from 'mui-datatables';
import Typography from '@material-ui/core/Typography';
import { Card, Row, Col } from 'reactstrap';
import Dialog from '@material-ui/core/Dialog';
import Grid from '@material-ui/core/Grid';
import { Tabs, TabPanel, Choice } from '@cmsgov/design-system-core';
import { disableManageRequestButton, enableRequestButton } from '../../shared/layout/header/nav-bar.reducer';
import _ from 'lodash';
import http from '../../shared/service/http-service';

const cancelRequest = { duaNumber: '', requestId: '', createDate: '', requestStatus: '', dataDescription: '', requestDesc: '' };
const cancelledRequest = {};
let childs = [];
let num = 0;
const copyStr = 'Copy';
const cancelStr = 'Cancel';
const deleteStr = 'Delete';
const cancelationConfimatiomMsg = 'Cancellation request has been sent for';
const copyConfirmationMsg = 'A copy of request Id';
const delteSucessMsg = 'Has been deleted successfully';
const copyConfirmationMsg2 = 'has been created and saved in drafts';
const requestCounter = null;
const pendingApprovalSts = 'PENDING APPROVAL';
const submittedSts = 'SUBMITTED';
const cancelRequestedSts = 'CANCEL REQUESTED';
const cancelledStr = 'CANCELLED';
const deniedStr2 = 'DENIED';
const draft = 'DRAFT';
let counter = 0;
const submitted = 'SUBMITTED';
const successfulStr = 'Successful';
const completedStr = 'Completed';
const deniedStr = 'Denied';
const copySmStr = 'copy';
const cancelSmStr = 'cancel';
const deleteSmStr = 'delete';
const superStr = 'SUPER';
// styles
const manage_button = {
  width: '130px',
  height: '40px'
};
const getColors = (statusDetail: string): string => {
  if (statusDetail === successfulStr || statusDetail === completedStr) {
    return 'oval-green';
  }
  if (statusDetail === deniedStr || statusDetail === cancelRequestedSts || statusDetail === deniedStr2 || statusDetail === cancelledStr) {
    return 'oval-red';
  }
  if (statusDetail === pendingApprovalSts || statusDetail === submittedSts) {
    return 'oval-yellow';
  }
};

const getConfirmation = (confirm: boolean): string => (confirm ? null : 'none');
const getAlertConfirmation = (confirm: boolean): string => (confirm ? null : 'none');
const requestOn = (fromtab: boolean): string => (fromtab ? 'Requested On' : 'Saved On');
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
export class ManageRequest extends React.Component<DispatchProps> {
  state = {
    requests: [],
    alertOpen: false,
    buttonDisabled: true,
    showPropertySection: false,
    rowData: [],
    rowMeta: { rowIndex: 0, rowNumber: 0 },
    currentRowsSelected: [],
    open: false,
    tableOptions: false,
    fromNewRequestComponent: false,
    toNewRequest: false,
    selectedRequestId: '',
    onClose: '',
    statusValues: '',
    newData: [],
    testData: false,
    requestData: [],
    draftData: [],
    noShow: true,
    ShowConfirmation: false,
    showAlertConfirmation: false,
    fromCopy: false,
    fromDraft: false,
    fromSubmitted: false,
    selectedIds: '',
    prevSelectedId: '',
    copyData: [],
    copied: false,
    alertText: '',
    submittedRequestCounter: 0,
    draftRequestCounter: 0,
    confirmationMessage: '',
    alertConfirmation: '',
    isChecked: '',
    disableCancel: true,
    disableCopy: true,
    buttonOn: false,
    notCompleted: 0,
    rowSelector: false,
    checkedRadio: null,
    forSubmittedTab: true,
    deleteDraft: false,
    cancelled: false,
    requestDataArray: [],
    copyStr: '',
    controllCheckedStatus: [],
    newCopy: [],
    copyRequestIds: [],
    apiRequest: [],
    newRequests: [],
    superRequests: [],
    subRequests: [],
    submittedRequestsFromBDC: []
  };
  getData = async () => {
    const { data: requests } = await http.get(`api/user-dua-requests`);
    this.setState({ requests });
    const superRequest = _.filter(this.state.requests, items => items.requestStatus === superStr);
    this.setState({ superRequests: superRequest });

    for (const val of superRequest) {
      const childRequest = _.filter(this.state.requests, item => item.superId === val.superId);
      childs = [...childs, childRequest];
    }
    this.setState({ subRequest: childs });
  };
  getSubmittedRequestData = () => {
    http
      .get(`api/user-dua-requests`)
      .then(response => {
        this.setState({ submittedRequestsFromBDC: response.data, loading: false, showAddNewsNotifcation: false });
      })
      .catch(error => {
        this.setState({ submittedRequestsFromBDC: [], loading: false });
        console.error('error api call to api/user-requests ' + error);
      });
  };
  postCancelRequest = () => {
    const id = [];
    id.push(this.state.rowData[1]);
    http
      .post(`api/cancel-request/` + this.state.rowData[2])
      .then(response => {
        //  responseErrorCode = response.data;
        // console.log('response error', responseErrorCode);
      })
      .catch(error => {
        console.error('error api call to api/cancel-request ' + error);
      });
  };
  postData = async () => {
    await http.put('api/cancel-request', cancelledRequest);
  };
  componentDidMount() {
    this.getSubmittedRequestData();
    this.props.disableManageRequestButton();
    this.props.enableRequestButton();
    this.setState({ requestData: this.state.requests });
  }

  handleRadioCheckedStatus = () => {
    let checkedStatus = [];
    for (const val of this.state.requests) {
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
  // handlers
  handleYes = value => {
    this.handleRadioCheckedStatus();
    if (value === copyStr) {
      this.copyRequest();
    }
    if (value === cancelStr) {
      this.cancelRequestSend();
    }
    if (value === deleteStr) {
      this.delteDraftHandler();
    }
  };
  handleOpenAlertDialog = event => {
    this.setState({ checkedRadio: event.target.value });

    this.setState({ selectedRequestId: this.state.rowData[2] });
    this.setState({ alertOpen: true });
    this.setState({ alertText: cancelStr });
    this.setState({ copyStr: cancelSmStr });
  };
  handleOpenCancelDialog = event => {
    this.setState({ isChecked: event.target.value });
    this.setState({ checkedRadio: event.target.value });
    counter = counter + 1;
    counter > 1 ? this.setState({ disableCancel: true }) : this.setState({ disable: false });
    const statusValue = this.state.rowData[4].props.children.props.children[1].props.children[0];
    statusValue === pendingApprovalSts || statusValue === submittedSts
      ? this.setState({
          disableCancel: false,
          disableCopy: false
        })
      : this.setState({ disableCopy: false });
    this.setState({ ShowConfirmation: false });
    this.setState({ showAlertConfirmation: false });
    this.setState({ selectedRequestId: event.target.id });
  };
  handleButtonEnable = () => {
    this.setState({ buttonDisabled: false });
  };
  handleClose = () => {
    this.setState({ alertText: '' });
    this.setState({ toNewRequestLanding: true });
    this.setState({ alertOpen: false });
    this.setState({ buttonDisabled: true });
  };

  handleDraftTabPanel = () => {
    this.setState({ fromDraft: true });
  };
  handleOpenAlertDialogForCopy = value => {
    this.setState({ selectedRequestId: this.state.rowData[2] });
    this.setState({ alertText: copyStr });
    this.setState({ copied: true });
    this.setState({ alertOpen: true });
    this.setState({ copyStr: copySmStr });
  };
  handleOpenAlertDialogForDelete = value => {
    this.setState({ selectedRequestId: this.state.rowData[2] });
    this.setState({ alertText: deleteStr });
    this.setState({ deleteDraft: true });
    this.setState({ alertOpen: true });
    this.setState({ copyStr: deleteSmStr });
  };
  delteDraftHandler = () => {
    this.setState({ draftRequestCounter: this.state.draftRequestCounter - 1 });
    this.setState({ alertOpen: false });
    this.setState({ disableCancel: true, disableCopy: true });
    const index = this.state.rowMeta.rowIndex;
    this.setState({ newCopy: [...this.state.newCopy.filter(item => item !== this.state.newCopy[index])] });
    this.setState({ alertText: '' });
    this.setState({
      ShowConfirmation: true,
      confirmationMessage: copyConfirmationMsg + ' ' + this.state.selectedRequestId + ' ' + delteSucessMsg
    });
  };
  cancelRequestSend = () => {
    this.postCancelRequest();
    /*const cancelRequestFilter = _.filter(this.state.requests, item => item.requestId === this.state.rowData[2]);
    cancelRequestFilter[0].requestStatus = cancelRequestedSts;
    cancelledRequest = cancelRequestFilter[0];*/

    /* cancelRequest.duaNumber = this.state.rowData[1];
    cancelRequest.requestStatus = cancelRequestedSts;
    cancelRequest.createDate = this.state.rowData[3];
    cancelRequest.requestId = this.state.rowData[2];
    cancelRequest.dataDescription = this.state.rowData[5];
    cancelRequest.requestDesc = this.state.rowData[6];
    const request = this.state.requests;
    const filteredRequest = _.filter(request, item => item.requestId !== cancelRequest.requestId);
    this.setState({ requests: [...filteredRequest, cancelRequest] });
    this.setState({ newRequests: [...filteredRequest, cancelRequest] });*/

    this.setState({ ShowConfirmation: true });
    this.setState({ confirmationMessage: cancelationConfimatiomMsg + ' ' + this.state.selectedRequestId });
    this.setState({ alertOpen: false });
    this.setState({ alertText: '' });
    this.setState({ disableCancel: true, disableCopy: true });

    // this.postData();
    this.setState({ alertOpen: false });
  };
  handleCheckedStatus = () => {
    this.setState({ controllCheckedStatus: [] });
  };
  copyRequest = () => {
    let letteri;
    const letters = [
      'A',
      'B',
      'C',
      'D',
      'E',
      'F',
      'G',
      'H',
      'I',
      'J',
      'K',
      'L',
      'M',
      'N',
      'O',
      'P',
      'Q',
      'R',
      'S',
      'T',
      'U',
      'V',
      'W',
      'X',
      'Y',
      'Z'
    ];
    if (this.state.copied) {
      letteri = letters[num];
    }
    num++;
    const copyRequest = { id: '', duaNumber: '', requestId: '', createDate: '', requestStatus: '', dataDescription: '', requestDesc: '' };
    copyRequest.id = this.state.rowData[2];
    copyRequest.duaNumber = this.state.rowData[1];
    copyRequest.requestStatus = this.state.rowData[4];
    copyRequest.createDate = currentDate;
    copyRequest.requestId = this.state.rowData[2] + letteri;
    copyRequest.dataDescription = this.state.rowData[5];
    copyRequest.requestDesc = this.state.rowData[6];
    this.setState({ newCopy: [...this.state.newCopy, copyRequest] });
    this.setState({
      ShowConfirmation: true,
      confirmationMessage: copyConfirmationMsg + ' ' + this.state.selectedRequestId + ' ' + copyConfirmationMsg2
    });

    this.setState({ alertOpen: false });
    this.setState({ copied: false });
    this.setState({ disableCancel: true, disableCopy: true, isChecked: '', alertText: '' });
  };
  render() {
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
                      id={this.state.rowData[2]}
                      name="select"
                      type="radio"
                      checked={this.state.controllCheckedStatus[this.state.rowMeta.rowIndex]}
                      onChange={this.handleCheckedStatus}
                      onClick={this.handleOpenCancelDialog}
                    />
                    <Typography variant="srOnly">
                      <label htmlFor={this.state.rowData[2]}>{''}</label>
                    </Typography>
                  </fieldset>
                </div>
              </React.Fragment>
            );
          }
        }
      },
      'DUA Number',
      'Request Id',
      { name: requestOn(this.state.forSubmittedTab) },
      {
        name: 'Status Details',
        options: {
          // tslint:disable-next-line:ter-arrow-body-style
          customBodyRender: value => {
            return (
              <React.Fragment>
                <fieldset className="ds-c-fieldset ds-u-margin-top--0">
                  <span className={getColors(value)} />
                  <span style={{ fontSize: '14px', color: 'black', marginLeft: '5px' }}>{value} </span>
                </fieldset>
              </React.Fragment>
            );
          }
        }
      },
      'Desc. Code',
      'Request Description'
    ];
    const options = {
      filter: true,
      download: false,
      viewColumns: true,
      search: true,
      selectableRows: false,
      responsive: 'scroll',
      print: false,
      pagination: true,
      elevation: 0,
      rowsPerPage: 5,
      rowsPerPageOptions: [5, 10, 15],
      rowHover: true,
      selectable: true,
      onRowClick: (rowData, rowMeta) => {
        this.setState({ rowData });
        this.setState({ checkedRadio: this.state.rowMeta.rowIndex });
        this.setState({ rowMeta });
      },
      onRowsSelect: currentRowsSelected => {
        this.setState({ currentRowsSelected });
      }
    };
    const { requests, submittedRequestsFromBDC } = this.state;
    const { newCopy } = this.state;
    const requestsData = _.filter(submittedRequestsFromBDC, item => item.requestStatus !== 'SUPER');
    const copyDa = _.filter(newCopy, item => item.status !== '');
    const dataArrayActive = submittedRequestsFromBDC.reduce<any[]>((all, data) => {
      all.push([
        data.requestId,
        data.duaNumber,
        data.requestId,
        data.createDate,
        data.requestStatus,
        data.dataDescription,
        data.requestDesc
      ]);
      return all;
    }, []);
    const dataArrayCopy = copyDa.reduce<any[]>((all, data) => {
      all.push([
        data.requestId,
        data.duaNumber,
        data.requestId,
        data.createDate,
        data.requestStatus,
        data.dataDescription,
        data.requestDesc
      ]);
      return all;
    }, []);
    return (
      <React.Fragment>
        <Dialog maxWidth="sm" open={this.state.alertOpen} onClose={this.handleClose} aria-labelledby="dialog-title">
          <div data-demo="This div is for demo purposes only">
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
                  <p className="ds-text">Are you sure you want to {this.state.copyStr} this request?</p>
                </main>
                <aside className="ds-c-dialog__actions" role="complementary">
                  <button
                    className="ds-c-button ds-c-button--primary"
                    value={this.state.alertText}
                    onClick={() => {
                      this.handleYes(this.state.alertText);
                    }}
                  >
                    Yes
                  </button>
                  <span id="dialogClose" className="pl-5">
                    <button className="ds-c-button ds-c-button--transparent" onClick={this.handleClose}>
                      Cancel
                    </button>
                  </span>
                </aside>
              </div>
            </div>
          </div>
        </Dialog>

        <Row>
          <Col xs="8" sm="11" className="col-manage-request">
            <Card body>
              <div className="manage-requests">
                Manage Request
                {'(s)'}
              </div>
              <Tabs
                defaultSelectedId="mange-request"
                onChange={(selectedId, prevSelectedId) => {
                  this.setState({ selectedIds: prevSelectedId });
                  this.setState({ forSubmittedTab: !this.state.forSubmittedTab });
                }}
              >
                <TabPanel
                  disabled={this.state.newCopy.length !== 0 ? false : true}
                  id="draft"
                  tab={
                    <div className="draft">
                      {draft + ' ' /*'(' + this.state.draftRequestCounter + ')'*/}
                      {<span className="ds-c-badge">{this.state.newCopy.length}</span>}
                    </div>
                  }
                >
                  <Row className="mb-0 mt-4">
                    <Col xs="8" sm="8" md="8">
                      <div
                        className="ds-c-alert ds-c-alert--success"
                        style={{ display: getConfirmation(this.state.ShowConfirmation), height: '20px' }}
                      >
                        <div className="ds-c-alert__body">
                          <p className="usa-alert-text">{this.state.confirmationMessage}</p>
                        </div>
                      </div>
                    </Col>
                  </Row>
                  <Row className="mb-0 mt-0">
                    <Col xs="8" sm="8" md="8">
                      <div
                        className="usa-alert usa-alert-warning"
                        style={{ display: getAlertConfirmation(this.state.showAlertConfirmation) }}
                      >
                        <div className="usa-alert-body">
                          <p className="usa-alert-text">{this.state.alertConfirmation}</p>
                        </div>
                      </div>
                    </Col>
                  </Row>
                  <Row>
                    <Col className="text-right">
                      <button
                        className="ds-c-button ds-c-button--primary"
                        disabled
                        style={manage_button}
                        onClick={value => {
                          this.handleOpenAlertDialog(value);
                        }}
                      >
                        Edit
                      </button>
                      {/*<button
                        value={this.state.rowData[0]}
                        disabled={this.state.disableCopy}
                        className="ds-c-button ds-c-button--primary"
                        style={manage_button}
                        onClick={value => {
                          this.handleOpenAlertDialogForDelete(value);
                        }}
                      >
                        Delete
                      </button>*/}
                    </Col>
                  </Row>
                  <Row>
                    <Col xs="6" sm="6" style={{ paddingBottom: '10px' }} />
                  </Row>
                  <MuiThemeProvider theme={theme}>
                    <MUIDataTable title="" data={dataArrayCopy} columns={columns} options={options} />
                  </MuiThemeProvider>
                </TabPanel>
                <TabPanel
                  id="mange-request"
                  tab={
                    <div className="submitted">
                      {submitted + ' ' /* + '(' + this.state.submittedRequestCounter + ')'*/}
                      {<span className="ds-c-badge">{dataArrayActive.length}</span>}
                    </div>
                  }
                >
                  <Row className="mb-0 mt-4">
                    <Col xs="8" sm="8" md="8">
                      <div
                        className="ds-c-alert ds-c-alert--success"
                        style={{ display: getConfirmation(this.state.ShowConfirmation), height: '20px' }}
                      >
                        <div className="ds-c-alert__body">
                          <p className="usa-alert-text">{this.state.confirmationMessage}</p>
                        </div>
                      </div>
                    </Col>
                  </Row>
                  <Row className="mb-0 mt-0">
                    <Col xs="8" sm="8" md="8">
                      <div
                        className="usa-alert usa-alert-warning"
                        style={{ display: getAlertConfirmation(this.state.showAlertConfirmation) }}
                      >
                        <div className="usa-alert-body">
                          <p className="usa-alert-text">{this.state.alertConfirmation}</p>
                        </div>
                      </div>
                    </Col>
                  </Row>
                  <Row>
                    <Col className="text-right">
                      <button
                        className="ds-c-button ds-c-button--primary"
                        disabled={this.state.disableCancel}
                        style={manage_button}
                        onClick={value => {
                          this.handleOpenAlertDialog(value);
                        }}
                      >
                        Cancel
                      </button>
                      <button
                        value={this.state.rowData[0]}
                        disabled={this.state.disableCopy}
                        className="ds-c-button ds-c-button--primary"
                        style={manage_button}
                        onClick={value => {
                          this.handleOpenAlertDialogForCopy(value);
                        }}
                      >
                        Copy
                      </button>
                      {/*<button disabled className="ds-c-button ds-c-button--primary" style={manage_button}>
                        Summary
                      </button>*/}
                    </Col>
                  </Row>
                  <Row>
                    <Col xs="6" sm="6" style={{ paddingBottom: '10px' }} />
                  </Row>
                  <MuiThemeProvider theme={theme}>
                    <MUIDataTable
                      title=""
                      data={this.state.requestDataArray.length === 0 ? dataArrayActive : this.state.requestDataArray}
                      columns={columns}
                      options={options}
                    />
                  </MuiThemeProvider>

                  <React.Fragment>
                    <Row>
                      <ul className="list-horizontal">
                        <span className="legend">Legend:</span>
                        {/*<span className="rectangle-legend" style={{ marginLeft: '20px' }}>
                  {' '}
                </span>
                <span className="super-requests">Super requests</span>*/}
                        <span className="oval-yellow" style={{ marginLeft: '20px' }} />
                        <span className="not-completed">Not completed</span>
                        <span className="oval-green" style={{ marginLeft: '20px' }} />
                        <span className="completed">Completed</span>
                        <span className="oval-red" style={{ marginLeft: '20px' }} />
                        <span className="cancelled">Cancelled/Denied</span>
                      </ul>
                    </Row>
                  </React.Fragment>
                </TabPanel>
              </Tabs>
            </Card>
          </Col>
        </Row>
      </React.Fragment>
    );
  }
}
const mapDispatchToProps = { disableManageRequestButton, enableRequestButton };

type DispatchProps = typeof mapDispatchToProps;

export default connect(
  null,
  mapDispatchToProps
)(ManageRequest);
