import '../admin.scss';
import React from 'react';
import { connect } from 'react-redux';
import Typography from '@material-ui/core/Typography';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import ToggleDisplay from 'react-toggle-display';
import { Tabs, TabPanel, Choice, ChoiceList } from '@cmsgov/design-system-core';
import MUIDataTable from 'mui-datatables';
import { Card, Row, Col } from 'reactstrap';
import { enableRequestButton, enableManageRequestButton } from '../../../shared/layout/header/nav-bar.reducer';
import _ from 'lodash';
import http from '../../../shared/service/http-service';
import LoadingOverlay from 'react-loading-overlay';
import { FadeLoader } from 'react-spinners';

export interface IManageApprovalseProp extends StateProps, DispatchProps {}
const approvedStr = 'approved';
const deniedStr = 'denied';
const adminStr = 'Admin';
let signedOutRequestId = [];
let approvalRequestId = [];
let responseErrorCode = null;
const today = new Date();
const dd = today.getDate();
let ddStr = String(dd);
if (dd < 10) ddStr = 0 + ddStr;
const mm = today.getMonth() + 1;
let mmStr = String(mm);
if (mm < 10) mmStr = 0 + mmStr;
const yyyy = today.getFullYear();
const currentDate = mmStr + '/' + ddStr + ' /' + yyyy;
let selectionCounter = 0;
let approvalSelectionCounter = 0;
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
const selectChoices = [
  { label: 'DENIED', value: '200' },
  { label: 'DENIED - REFERRED TO BESS', value: '210' },
  { label: 'DENIED - REFERRED TO DENOM', value: '215' },
  { label: 'DENIED - REFERRED TO EDB', value: '220' },
  { label: 'DENIED - REFERRED TO HCIS', value: '225' },
  { label: 'DENIED - REFERRED TO MBD', value: '230' },
  { label: 'DENIED - REFERRED TO MEDPAR', value: '235' },
  { label: 'DENIED - REFERRED TO NMUD', value: '240' },
  { label: 'DENIED - REFERRED TO SAF', value: '245' },
  { label: 'DENIED - REFERRED TO SPCL PGM', value: '250' }
];

const childSelect = (
  <ChoiceList
    choices={selectChoices}
    label="Select a denial reason(*)"
    labelClassName="ds-c-label ds-u-margin-top--0"
    name="select_choices_field"
  />
);
export class ManageApprovals extends React.Component<IManageApprovalseProp> {
  state = {
    approvals: [],
    rowMeta: { rowIndex: 0, rowNumber: 0 },
    rowData: [],
    showManageApprovals: true,
    enableSummary: true,
    enableSignOut: true,
    isChecked: [],
    dataArrayPendingAfterSignOut: [],
    pendingFinal: [],
    pendingApprovals: {
      id: 0,
      requestId: '',
      duaNum: '',
      submittedBy: '',
      submittedOn: '',
      status: '',
      approver: '',
      approvalDate: ''
    },
    approvalResults: [],
    showApprove: false,
    enableSubmit: true,
    actionTypes: '',
    signOut: [],
    requestId: [],
    signOutFinal: [],
    approvalRequestId: [],
    showConfirmation: false,
    appRequestId: '',
    showSignOutConfirmation: false,
    signOutAfterApproved: [],
    myApprovals: [],
    signedAfterApproval: [],
    multipleApproval: [],
    allApproved: [],
    checkToApprove: [],
    isCheckedSignOut: [],
    signOutTabClicked: false,
    approve: false,
    deny: false,
    checkedRadio: null,
    appChecked: [],
    loading: true,
    pendingFromBDC: [],
    signoutFromBDC: [],
    approvedRequestsData: [],
    deniedRequestData: [],
    approveOrDenyChecked: [],
    signoutError: []
  };

  getPendingApprovalsData = () => {
    http
      .get(`api/get-pending-requests`)
      .then(response => {
        this.setState({ pendingFromBDC: response.data, loading: false, showAddNewsNotifcation: false });
        this.setState({ pendingFinal: response.data });
      })
      .catch(error => {
        this.setState({ approvals: [], loading: false });
        console.error('error api call to api/get-pending-requests ' + error);
      });
  };

  getApprovedRequestData = () => {
    http
      .get(`api/get-approved-requests`)
      .then(response => {
        this.setState({ approvedRequestsData: response.data, loading: false, showAddNewsNotifcation: false });
      })
      .catch(error => {
        this.setState({ approvedRequestsData: [], loading: false });
        console.error('error api call to api/get-approved-requests ' + error);
      });
  };
  getDeniedRequestData = () => {
    http
      .get(`api/get-denied-requests`)
      .then(response => {
        this.setState({ deniedRequestData: response.data, loading: false, showAddNewsNotifcation: false });
      })
      .catch(error => {
        this.setState({ deniedRequestData: [], loading: false });
        console.error('error api call to api/get-approved-requests ' + error);
      });
  };
  getSignoutData = () => {
    http
      .get(`api/get-singout-requests`)
      .then(response => {
        this.setState({ signoutFromBDC: response.data, loading: false, showAddNewsNotifcation: false });
      })
      .catch(error => {
        this.setState({ signoutFromBDC: [], loading: false });
        console.error('error api call to api/get-pending-requests ' + error);
      });
  };
  getApprovalsData = () => {
    http
      .get(`api/get-pending-requests`)
      .then(response => {
        this.setState({ allApproved: response.data, loading: false, showAddNewsNotifcation: false });
      })
      .catch(error => {
        this.setState({ allApproved: [], loading: false });
        console.error('error api call to api/get-pending-requests ' + error);
      });
  };
  postSignoutRequest = () => {
    const id = [];
    id.push(this.state.rowData[1]);
    http
      .post(`api/update-approval-requests/` + id + '/' + '410')
      .then(response => {
        responseErrorCode = response.data;
        console.log('response error', responseErrorCode);
      })
      .catch(error => {
        console.error('error api call to api/update-approval-requests ' + error);
      });
  };
  postApproveOrDeny = () => {
    const statusCode = this.state.actionTypes;
    const id = [];
    id.push(this.state.approvalRequestId);

    http
      .post(`api/update-approval-requests/` + id + '/' + statusCode)
      .then(response => {
        responseErrorCode = response.data;
      })
      .catch(error => {
        console.error('error api call to api/update-approval-requests ' + error);
      });
  };
  postApprovals = () => {
    const id = this.state.approvalRequestId;
    http
      .post(`api/update-approval-requests/` + id + '/' + '300')
      .then(response => {
        console.log(response.data);
      })
      .catch(error => {
        console.error('error api call to api/update-approval-requests ' + error);
      });
  };
  componentDidMount() {
    this.getPendingApprovalsData();
    this.getSignoutData();
    this.getApprovedRequestData();
    this.getDeniedRequestData();
    this.setState({ rowData: this.state.approvals });
    this.props.enableManageRequestButton();
    this.props.enableRequestButton();
    selectionCounter = 0;
    approvalSelectionCounter = 0;
  }
  requestApprove = () => {
    this.postApproveOrDeny();
    this.getSignoutData();
    this.getDeniedRequestData();
    this.getApprovedRequestData();
    approvalSelectionCounter = 0;
    this.setState({ signedAfterApproval: this.state.myApprovals });
    const status = this.state.actionTypes;
    const myApprovals = this.state.myApprovals;
    const multipleApprovals = this.state.multipleApproval;
    const approvalRId = this.state.approvalRequestId;
    const statusCheck = status !== approvedStr ? deniedStr : status;
    for (const val of approvalRId) {
      const appRes = { id: 0, requestId: '', duaNum: '', submittedBy: '', submittedOn: '', status: '', approver: '', approvalDate: '' };
      const objIndex = myApprovals.findIndex(obj => obj.requestId === val);
      appRes.id = myApprovals[objIndex].id;
      appRes.requestId = myApprovals[objIndex].requestId;
      appRes.duaNum = myApprovals[objIndex].duaNum;
      appRes.submittedBy = myApprovals[objIndex].submittedBy;
      appRes.submittedOn = myApprovals[objIndex].submittedOn;
      appRes.status = statusCheck;
      appRes.approver = adminStr;
      appRes.approvalDate = currentDate;
      this.setState({ multipleApproval: [...multipleApprovals, appRes] });
      this.state.allApproved.push(appRes);
    }
    let requestIdList = [];
    requestIdList = this.state.approvalRequestId;
    let pendingApprovalList = this.state.signOutFinal;
    for (const val of requestIdList) {
      pendingApprovalList = _.filter(pendingApprovalList, item => item.requestId !== val);
      this.setState({ signOutFinal: pendingApprovalList });
      this.setState({ signOutFinal: pendingApprovalList });
    }
    this.setState({ showConfirmation: true });
    approvalSelectionCounter = 0;
    this.setState({ showApprove: false });
    approvalRequestId = this.state.approvalRequestId;
    let checkedStatus = [];
    for (const r of this.state.signoutFromBDC) {
      const addFalse = this.state.checkToApprove.push(false);
      this.state.approveOrDenyChecked.push(false);
      checkedStatus = [...this.state.checkToApprove];
    }
    this.setState({ checkToApprove: checkedStatus });
    this.setState({ approveOrDenyChecked: checkedStatus });

    this.setState({ approvalRequestId: [] });

    setTimeout(() => {
      this.setState({ checkToApprove: [] });
      this.setState({ approveOrDenyChecked: [] });
    }, 10);
    this.postApprovals();
  };
  handleSignOut = () => {
    this.setState({ showConfirmation: false, showSignOutConfirmation: false });
    const appSignedOut = { id: null, requestId: '', duaNum: '', submittedBy: '', submittedOn: '', DUAStudyName: '' };
    const index = this.state.rowMeta.rowIndex;
    appSignedOut.id = index;
    appSignedOut.requestId = this.state.rowData[1];
    appSignedOut.duaNum = this.state.rowData[2];
    appSignedOut.submittedBy = this.state.rowData[3];
    appSignedOut.submittedOn = this.state.rowData[4];
    appSignedOut.DUAStudyName = this.state.rowData[5];
    selectionCounter++;
    for (const val of this.state.requestId) {
      const filterPending = _.filter(this.state.pendingFinal, item => item.superId !== val);
      this.setState({ pendingFinal: filterPending });
    }
    const rowReqId = this.state.rowData[1];
    this.setState({ signOut: appSignedOut });
    this.setState({ requestId: [rowReqId] });
    selectionCounter === 1
      ? this.setState({
          enableSummary: false,
          enableSignOut: false
        })
      : this.setState({ enableSummary: true });

    this.setState(() => {
      return { signOutTabClicked: false };
    });
  };
  moveToSignOut = () => {
    this.postSignoutRequest();
    this.getPendingApprovalsData();
    this.getSignoutData();
    selectionCounter = 0;
    this.setState({ moveToSignOut: true });
    this.setState({ signOutFinal: [...this.state.signOutFinal, ...this.state.signOut] });
    let requestIdList = [];
    requestIdList = this.state.requestId;
    let pendingApprovalList = this.state.approvals;

    for (const t of requestIdList) {
      pendingApprovalList = _.filter(pendingApprovalList, item => item.superId !== t);
      this.setState({ approvals: pendingApprovalList });
    }
    this.setState({ showSignOutConfirmation: true });
    this.setState({ enableSummary: true, enableSignOut: true });
    selectionCounter = 0;
    signedOutRequestId = this.state.requestId;
    let checkedStatus = [];
    for (const val of this.state.pendingFromBDC) {
      const addFalse = this.state.isChecked.push(false);
      checkedStatus = [...this.state.isChecked, addFalse];
    }
    this.setState({ isChecked: checkedStatus });
    this.setState({ requestId: [] });
    setTimeout(() => {
      this.setState({ isChecked: [] });
      this.setState({ checkToApprove: [] });
    }, 10);
    this.setState({ signOut: [] });
  };
  handleResetCunter = () => {
    let checkedStatus = [];
    let checkedApprove = [];
    for (const val of this.state.approvals) {
      const addFalse = this.state.isChecked.push(false);
      const addFalseToChkToApprove = this.state.checkToApprove.push(false);
      checkedStatus = [...this.state.isChecked, addFalse];
      checkedApprove = [...this.state.checkToApprove];
    }
    this.setState({ isChecked: checkedStatus });
    this.setState({ checkToApprove: checkedApprove });
    this.setState({ requestId: [] });
    this.setState({ approvalRequestId: [] });
    setTimeout(() => {
      this.setState({ isChecked: [] });
      this.setState({ checkToApprove: [] });
      this.setState({ enableSummary: true });
      this.setState({ enableSignOut: true });
      this.setState({ myApprovals: [] });
      this.setState({ showApprove: false });
      selectionCounter = 0;
      this.setState(() => {
        return { signOutTabClicked: true };
      });
      this.setState(() => {
        return { signOut: [] };
      });
    }, 10);
  };
  handleApprove = () => {
    const status = this.state.actionTypes;
    this.setState({ showApprove: true, showSignOutConfirmation: false, showConfirmation: false, enableSummary: false });

    const appRes = { id: 0, requestId: '', duaNum: '', submittedBy: '', submittedOn: '', status: '', approver: '', approvalDate: '' };

    appRes.id = this.state.rowMeta.rowIndex;
    appRes.requestId = this.state.rowData[1];
    appRes.duaNum = this.state.rowData[2];
    appRes.submittedBy = this.state.rowData[3];
    appRes.submittedOn = this.state.rowData[4];
    const statusCheck = status === approvedStr ? status : deniedStr;
    appRes.status = statusCheck;
    appRes.approver = adminStr;
    appRes.approvalDate = currentDate;
    const filterSimilar = _.filter(this.state.myApprovals, item => item.requestId === appRes.requestId);
    const filterDifference = _.filter(this.state.myApprovals, items => items.requestId !== appRes.requestId);
    const filterSimilarLength = filterSimilar.length;
    if (filterSimilarLength < 1) {
      const reqId = this.state.approvalRequestId;
      const rowReqId = this.state.rowData[1];
      this.setState({ myApprovals: [...this.state.myApprovals, appRes] });
      this.setState({ approvalRequestId: [...reqId, rowReqId] });
      approvalSelectionCounter++;
    } else {
      this.setState({ myApprovals: filterDifference });
      approvalSelectionCounter--;
      const approvalRequestIds = this.state.approvalRequestId;
      for (let i = 0; i < approvalRequestIds.length; i++) {
        if (this.state.rowData[1] === approvalRequestIds[i]) {
          const approvalRequestID2 = this.state.approvalRequestId;
          const removeUncheckedId = approvalRequestIds.splice(i, 1);
          const requestIdAfterUncheked = _.filter(approvalRequestID2, item => item !== removeUncheckedId);
          this.setState({ approvalRequestId: requestIdAfterUncheked });
        }
      }
    }
    this.setState({ appRequestId: appRes.requestId });

    approvalSelectionCounter === 1 ? this.setState({ enableSummary: false }) : this.setState({ enableSummary: true });
    approvalSelectionCounter === 0 ? this.setState({ showApprove: false }) : this.setState({ showApprove: true });
    this.setState({ signOutTabClicked: false });
    this.setState({ actionTypes: statusCheck });
  };
  turnOnSubmit = val => {
    this.setState({ actionTypes: val.target.value });
    this.setState({ appChecked: !this.state.appChecked });
    val.target.value !== deniedStr ? this.setState({ enableSubmit: false }) : this.setState({ enableSubmit: true });
  };
  handleCheckedStatus = () => {
    this.setState({ isChecked: [] }, () => this.setState({ checkToApprove: [] }));
  };
  approveChecked = () => {
    const approve = this.state.approve;
    const deny = this.state.deny;

    this.setState({ approve: !approve });
    if (this.state.deny) {
      this.setState({ deny: !deny });
    }
  };
  handleAppChecked = () => {
    this.setState({ appChecked: [] });
  };
  denyChecked = () => {
    const deny = this.state.deny;
    this.setState({ deny: !deny });
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
                <fieldset className="ds-c-fieldset">
                  <Choice
                    id={this.state.rowData[1]}
                    value={value}
                    checked={this.state.isChecked[this.state.rowMeta.rowIndex]}
                    onChange={this.handleCheckedStatus}
                    name="select"
                    type="radio"
                    multiple={false}
                    onClick={this.handleSignOut}
                  />
                  <label htmlFor={this.state.rowData[1]}>{''}</label>
                </fieldset>
              </React.Fragment>
            );
          }
        }
      },
      'Request Id',
      'DUA Number',
      'Submitted By',
      'Submitted On',
      'DUA Study Name'
    ];
    const signedOutcolumns = [
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
                <fieldset className="ds-c-fieldset" style={{ marginTop: '0px', marginBottom: '5px' }}>
                  <Choice
                    id={this.state.rowData[1]}
                    name="selects"
                    value={value}
                    checked={this.state.checkToApprove[this.state.rowMeta.rowIndex]}
                    onChange={this.handleCheckedStatus}
                    type="checkbox"
                    size="small"
                    onClick={this.handleApprove}
                  />
                  <label htmlFor={this.state.rowData[1]}>{''}</label>
                </fieldset>
              </React.Fragment>
            );
          }
        }
      },
      'Request Id',
      'DUA Number',
      'Submitted By',
      'Submitted On',
      'DUA Study Name'
    ];
    const approvalColumn = [
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
                <fieldset className="ds-c-fieldset" style={{ marginTop: '0px', marginBottom: '5px' }}>
                  <Choice id={this.state.rowData[1]} name={this.state.rowData[1]} disabled type="radio" size="small" />
                  <label htmlFor={this.state.rowData[1]}>{''}</label>
                </fieldset>
              </React.Fragment>
            );
          }
        }
      },
      'Request Id',
      'DUA Number',
      'Submitted By',
      'Submitted On',
      'Status',
      'Study Name'
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
        this.setState({ rowMeta });
      }
    };
    const signOuptOptions = {
      ...options,
      elevation: 0
    };

    const { approvedRequestsData, loading } = this.state;
    const approvalResultArray = approvedRequestsData.reduce<any[]>((all, data) => {
      all.push([data.superId, data.superId, data.duaNumber, data.submittedBy, data.submittedDate, 'Approved', data.studyName]);
      return all;
    }, []);
    const { deniedRequestData } = this.state;
    const deniedResultArray = deniedRequestData.reduce<any[]>((all, data) => {
      all.push([data.superId, data.superId, data.duaNumber, data.submittedBy, data.submittedDate, 'Denied', data.studyName]);
      return all;
    }, []);

    const { pendingFromBDC } = this.state;
    const dataArrayApprovals = pendingFromBDC.reduce<any[]>((all, data) => {
      all.push([data.superId, data.superId, data.duaNumber, data.submittedBy, data.submittedDate, data.studyName]);
      return all;
    }, []);
    const { signoutFromBDC } = this.state;
    const signOutDataArray = signoutFromBDC.reduce<any[]>((all, data) => {
      all.push([data.superId, data.superId, data.duaNumber, data.submittedBy, data.submittedDate, data.studyName]);
      return all;
    }, []);
    return (
      <React.Fragment>
        <ToggleDisplay show={this.state.showManageApprovals}>
          <Row>
            <Col xs="8" sm="11" className="col-manage-request">
              <Card body>
                <div className="manage-news">Manage Approvals</div>
                <ToggleDisplay show={this.state.showSignOutConfirmation && responseErrorCode === 0}>
                  <div className="ds-c-alert ds-c-alert--success">
                    <div className="ds-c-alert__body">
                      <p className="ds-c-alert__text">
                        {signedOutRequestId.length} request {''} has been signed out
                      </p>
                    </div>
                  </div>
                </ToggleDisplay>
                <Col
                  xs="8"
                  sm="8"
                  md="8"
                  className={this.state.signoutError && responseErrorCode > 0 ? 'ds-c-alert ds-c-alert--error' : 'hide'}
                >
                  <div className="ds-c-alert__body">
                    <h3 className="ds-c-alert__heading">
                      The system was unable to process the following approval section. please re-select and try again.
                    </h3>
                    <p className="ds-c-alert__text">
                      {' '}
                      If this problem presists, please contact the DESY Hot line at 410-786-0159 for futher asistance 16400
                    </p>
                  </div>
                </Col>
                <Tabs defaultSelectedId="pending" onChange={this.handleResetCunter}>
                  <TabPanel
                    id="pending"
                    tab={
                      <div className="active-news-admin">
                        {'Pending' + ' ' /* + '(' + this.state.submittedRequestCounter + ')'*/}
                        {<span className="ds-c-badge">{dataArrayApprovals.length}</span>}
                      </div>
                    }
                  >
                    <Row>
                      <Col className="text-right">
                        {/* <button className="ds-c-button ds-c-button--primary" disabled={this.state.enableSummary}>
                          Summary
                  </button>*/}

                        <button
                          value="value"
                          disabled={this.state.enableSignOut}
                          onClick={this.moveToSignOut}
                          className="ds-c-button ds-c-button--primary"
                        >
                          Sign out
                        </button>
                      </Col>
                    </Row>

                    <Row>
                      <Col>
                        <LoadingOverlay
                          active={loading}
                          styles={{ overlay: base => ({ ...base, background: 'rgba(0, 0, 0, 0.1)' }) }}
                          spinner={<FadeLoader color={'#4A90E2'} />}
                        >
                          <MuiThemeProvider theme={theme}>
                            <MUIDataTable
                              id="pending"
                              name="pending approvals"
                              title=""
                              data={
                                this.state.dataArrayPendingAfterSignOut.length === 0
                                  ? dataArrayApprovals
                                  : this.state.dataArrayPendingAfterSignOut
                              }
                              columns={columns}
                              options={options}
                            />
                          </MuiThemeProvider>
                        </LoadingOverlay>
                      </Col>
                    </Row>
                  </TabPanel>
                  <TabPanel
                    id="signed-out"
                    tab={
                      <div className="archive-news-admin">
                        {'Signed out' + ' ' /*'(' + this.state.draftRequestCounter + ')'*/}
                        {<span className="ds-c-badge">{String(signOutDataArray.length)}</span>}
                      </div>
                    }
                  >
                    <Row>
                      <Col className="text-right">
                        {/*<button value="active" disabled={this.state.enableSummary} className="ds-c-button ds-c-button--primary">
                          Summary{' '}
                  </button>*/}
                      </Col>
                    </Row>
                    <LoadingOverlay
                      active={loading}
                      styles={{ overlay: base => ({ ...base, background: 'rgba(0, 0, 0, 0.1)' }) }}
                      spinner={<FadeLoader color={'#4A90E2'} />}
                    >
                      <MuiThemeProvider theme={theme}>
                        <MUIDataTable
                          id="sign-out"
                          name="signed-out"
                          title=""
                          data={signOutDataArray}
                          columns={signedOutcolumns}
                          options={signOuptOptions}
                        />
                      </MuiThemeProvider>
                    </LoadingOverlay>
                    <Row>
                      <ToggleDisplay show={this.state.showConfirmation}>
                        <div className="ds-c-alert ds-c-alert--success">
                          <div className="ds-c-alert__body">
                            <p className="ds-c-alert__text">
                              {''} {approvalRequestId.length} requests has been {this.state.actionTypes === '300' ? 'approved' : 'denied'}
                              {''} and user has been notified
                            </p>
                          </div>
                        </div>
                      </ToggleDisplay>
                      <ToggleDisplay show={this.state.showApprove}>
                        <div className="mb-4" style={{ display: 'inline-block' }}>
                          <Typography variant="h6" style={{ color: '#205493', cursor: 'pointer' }} />
                        </div>
                        <fieldset className="ds-c-fieldset pl-5 mb-5 mt-5">
                          <legend className="ds-c-label">Would you like to approve or deny the selection? </legend>

                          <Choice
                            name="checkbox_choice_children_small"
                            size="small"
                            type="radio"
                            value="300"
                            checked={this.state.approveOrDenyChecked[2]}
                            onChange={this.handleAppChecked}
                            onClick={value => this.turnOnSubmit(value)}
                          >
                            <label style={{ paddingLeft: '13px' }}>Approve</label>
                          </Choice>
                          <Choice
                            name="checkbox_choice_children_small"
                            value="denied"
                            type="radio"
                            checked={this.state.approveOrDenyChecked[1]}
                            onChange={this.handleAppChecked}
                            onClick={value => this.turnOnSubmit(value)}
                            checkedChildren={
                              <div
                                onClick={value => this.turnOnSubmit(value)}
                                className="ds-c-choice__checkedChild ds-c-choice__checkedChild--small"
                              >
                                {childSelect}
                              </div>
                            }
                            uncheckedChildren={<div>{''}</div>}
                          >
                            <label>Deny</label>
                          </Choice>

                          <button
                            className="ds-c-button ds-c-button--primary"
                            disabled={this.state.enableSubmit}
                            onClick={this.requestApprove}
                          >
                            Submit
                          </button>
                        </fieldset>
                      </ToggleDisplay>
                    </Row>
                  </TabPanel>
                  <TabPanel
                    id="approval-results"
                    tab={
                      <div className="manage-approvals-approvals">
                        {'Approved requests' + ' ' /*'(' + this.state.draftRequestCounter + ')'*/}
                        {<span className="ds-c-badge">{String(this.state.approvedRequestsData.length)}</span>}
                      </div>
                    }
                  >
                    <Row>
                      <Col className="text-right">
                        {/*<button value="active" disabled className="ds-c-button ds-c-button--primary">
                          Summary{' '}
                </button>*/}
                      </Col>
                    </Row>
                    <LoadingOverlay
                      active={loading}
                      styles={{ overlay: base => ({ ...base, background: 'rgba(0, 0, 0, 0.1)' }) }}
                      spinner={<FadeLoader color={'#4A90E2'} />}
                    >
                      <MuiThemeProvider theme={theme}>
                        <MUIDataTable
                          id="approvals"
                          name="approval result"
                          title=""
                          data={approvalResultArray}
                          columns={approvalColumn}
                          options={options}
                        />
                      </MuiThemeProvider>
                    </LoadingOverlay>
                  </TabPanel>

                  <TabPanel
                    id="denied-results"
                    tab={
                      <div className="manage-approvals-approvals">
                        {'Denied requests' + ' ' /*'(' + this.state.draftRequestCounter + ')'*/}
                        {<span className="ds-c-badge">{String(this.state.deniedRequestData.length)}</span>}
                      </div>
                    }
                  >
                    <Row>
                      <Col className="text-right">
                        {/*<button value="active" disabled className="ds-c-button ds-c-button--primary">
                          Summary{' '}
                </button>*/}
                      </Col>
                    </Row>
                    <LoadingOverlay
                      active={loading}
                      styles={{ overlay: base => ({ ...base, background: 'rgba(0, 0, 0, 0.1)' }) }}
                      spinner={<FadeLoader color={'#4A90E2'} />}
                    >
                      <MuiThemeProvider theme={theme}>
                        <MUIDataTable
                          id="approvals"
                          name="approval result"
                          title=""
                          data={deniedResultArray}
                          columns={approvalColumn}
                          options={options}
                        />
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

type StateProps = ReturnType<null>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  null,
  mapDispatchToProps
)(ManageApprovals);
