import './request.scss';
import React from 'react';
import { Row, Col, Table } from 'reactstrap';
import { RouteComponentProps } from 'react-router-dom';
import ArrowBack from '@material-ui/icons/ArrowBack';
import { Typography } from '@material-ui/core';
import Paper from '@material-ui/core/Paper';
import _ from 'lodash';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import PrintProvider, { Print, NoPrint } from 'react-easy-print';
import { updateOutput } from './output.reducer';
import { connect } from 'react-redux';
import Divider from '@material-ui/core/Divider';
import http from '../../shared/service/http-service';
import { enableRequestButton } from '../../shared/layout/header/nav-bar.reducer';
import { IRootState } from '../../../app/shared/reducers';
import { getSession } from '../../../app/shared/reducers/authentication';

let property = {} as any;
let criteria = {} as any;
let output = {} as any;

export interface ISummaryProps extends StateProps, DispatchProps {}

class Summary extends React.Component<ISummaryProps & RouteComponentProps> {
  state = {
    summarySubmission: false,
    hideSubmitBtn: false,
    request: {} as any,
    todayDate: '',
    requestId: ''
  };
  componentDidMount() {
    this.props.getSession();
  }
  submitRequest = () => {
    const { request } = this.state;
    http
      .post('api/submit-request', request)
      .then(response => {
        this.setState({ requestId: response.data });
      })
      .catch(error => {
        console.error('error api call ' + `api/submit-request` + error);
      });
  };
  handleBackButton = () => {
    const { criteriaSetData } = this.props.location.state;
    this.props.updateOutput(output);
    this.props.history.push({
      pathname: '/new-request/' + property.dua,
      state: { property, criteria, criteriaSetData, showPropertySection: true, redirectedFromSummary: true }
    });
  };
  handleSubmitRequest = () => {
    property = this.props.location.state.property;
    criteria = this.props.location.state.criteria;
    output = this.props.location.state.output;
    const request = {
      superID: 0,
      requestID: 0,
      // approval: null,
      requestDesc: output.requestDescription,
      // dateCreated: null,
      // status: null,
      dataDescriptionCode: '',
      // prevRequestID: 0,
      commaDelimited: output,
      commaDelimitedDesc: 'Yes',
      dropRecordRequired: 'U',
      customViewID: 1,
      dateModified: null,
      outputFileIdentifier: 'DSYTST9',
      dataYear: null,
      dropRecordRequiredInt: 0,
      befPufView: true,
      searchCriteria: {
        filters: null,
        finderFiles: null
      },
      subRequests: [
        // {
        //   requestID: 0,
        //   dataYear: 1998,
        //   outputFileName: null,
        //   outputCopybook: null,
        //   recordCount: null,
        //   droppedRecordCount: null,
        //   droppedFileName: null,
        //   requestStatus: null,
        //   updateMonthYear: null,
        //   completed: false
        // },
        // {
        //   requestID: 0,
        //   dataYear: 1999,
        //   outputFileName: null,
        //   outputCopybook: null,
        //   recordCount: null,
        //   droppedRecordCount: null,
        //   droppedFileName: null,
        //   requestStatus: null,
        //   updateMonthYear: null,
        //   completed: false
        // }
      ],
      outputColumns: [
        {
          columnID: 84,
          name: 'xz'
        }
      ],
      filter: [
        {
          columnID: 84,
          operator: '=',
          value: '19100101',
          columnName: null,
          groupID: 1,
          squenceID: 0,
          lookupText: null,
          whereClause: null,
          eleIcdIndCd: null,
          lookupTextOnly: null
        },
        {
          columnID: 84,
          operator: 'USER INPUT FILE',
          value: 'USER INPUT FILE',
          columnName: null,
          groupID: 2,
          squenceID: 0,
          lookupText: null,
          whereClause: null,
          eleIcdIndCd: null,
          lookupTextOnly: null
        }
      ],
      finderFileDTOLst: [
        {
          fileName: 'My file',
          startPosition: 12,
          headerStartPosition: 1,
          finderColumn: {
            columnID: 84,
            name: 'xz'
          },
          groupID: 2,
          filIcdIndCd: null
        },
        {
          fileName: 'My file',
          startPosition: 12,
          headerStartPosition: 1,
          finderColumn: {
            columnID: 84,
            name: 'xz'
          },
          groupID: 1,
          filIcdIndCd: null
        }
      ],
      formattedTimestampDateCreated: null,
      formattedDateModified: null,
      formattedDateCreated: null,
      state: {
        stateCode: '21',
        description: 'MARYLAND'
      },
      mediaType: {
        mediaTypeID: 3,
        description: '3490 COMPRESSED'
      },
      dataType: {
        dataTypeID: 4,
        name: 'ALL TYPES',
        dataSourceYears: null
      },
      duaDetails: {
        duaNumber: 12987,
        expirationDate: null,
        requestor: null,
        studyName: 'DSY-CONTRACT-DEV - 09, DO NOT CHG W/O EMAILING DESY_SUPPORT@CMS.HHS.GOV',
        returnRequired: true,
        encryptionSwitch: '\u0000',
        encryptionDesc: null,
        ftapeSwitch: '\u0000',
        desyExpirationDate: 1556769600000,
        duaExpDays: -7,
        duaToExpireMessage: null,
        formattedExpirationDate: null,
        formattedDesyExpirationDate: '05/02/2019',
        foriegnTape: false,
        supressFlag: false,
        duaClosed: false,
        duaToExpire: false,
        duaExpired: true
      },
      superRequest: true,
      dataSource: {
        dataSourceId: 1,
        name: 'NCH NEARLINE FILE',
        states: null,
        dataTypes: null
      },
      userDetails: {
        userId: 'NDOM',
        password: 'Mypass',
        userName: 'JAGANNATHAN NARASHIMMAN',
        email: 'aaa@aaa',
        uniqueId: '-4721c142:16a9fbf1dcd:-8000',
        userNum: 583
      },
      format: null,
      outputType: {
        viewID: 'V2',
        description: 'RQST-OTPT-DESC',
        viewType: '\u0000',
        viewIDInt: -1
      },
      savedRequest: true,
      recipient: null
    };

    http
      .post(`api/submit-request`, request)
      .then(response => {
        this.setState({ requestId: response.data });
      })
      .catch(error => {
        console.error('error api call ' + `api/submit-request` + error);
      });
    this.props.enableRequestButton();
    this.setState({ summarySubmission: true });
    this.setState({ hideSubmitBtn: true });
    this.getCurrentDate();
  };
  getCurrentDate = () => {
    const today = new Date();
    let dd = today.getDate();
    let mm = today.getMonth() + 1; // As January is 0.
    const yyyy = today.getFullYear();

    if (dd < 10) dd = 0 + dd;
    if (mm < 10) mm = 0 + mm;
    const todayDate = mm + '/' + dd + '/' + yyyy;
    this.setState({ todayDate });
  };

  render() {
    const { account } = this.props;
    const state = property.state === '' || property.state === undefined ? 'N/A' : property.state;
    const repeat = criteria.field === undefined || criteria.field.length === 0 ? 0 : criteria.field.length;

    if (this.props.location.state !== undefined) {
      property = this.props.location.state.property;
      criteria = this.props.location.state.criteria;
      output = this.props.location.state.output;
    }

    return (
      <React.Fragment>
        <PrintProvider>
          <Paper className="summary-wrapper" elevation={0}>
            <Row className="float-right">
              <Col className="text-right">
                <span onClick={() => window.print()} style={{ cursor: 'pointer' }}>
                  <FontAwesomeIcon title="print summary" color="#0071bb" size="2x" icon="print" />
                  <span className="print">Print</span>
                </span>
              </Col>
            </Row>
            <Print single name="summary">
              <Row className={this.state.summarySubmission ? 'hide' : 'show w-50'}>
                <NoPrint>
                  <Col xs="6" sm="6" md="6">
                    <Typography variant="h6" style={{ color: '#205493', cursor: 'pointer' }} onClick={this.handleBackButton}>
                      <ArrowBack /> <u>Back</u>
                    </Typography>
                  </Col>
                </NoPrint>

                <Col xs="12" sm="12" md="12" className="pl-3 mb-4">
                  <h4 className="summary"> Summary </h4>
                </Col>
              </Row>

              <Row className={this.state.summarySubmission ? 'show  mb-5 w-75' : 'hide'}>
                <Col xs="12" sm="12" md="12" style={{ marginBottom: '4rem' }}>
                  <h4 className="summary"> Request Confirmation </h4>
                </Col>
              </Row>
              <Row className={this.state.summarySubmission ? 'show  mb-5' : 'hide'}>
                <div className="pl-3 mb-5">
                  <Col xs="12" sm="12" md="12" className="ds-c-alert ds-c-alert--success w-75">
                    <div className="ds-c-alert__body">
                      <h3 className="ds-c-alert__heading">Your request has been submitted</h3>
                      <p className="ds-c-alert__text">You can access the request details in the 'Manage Requests' section</p>
                    </div>
                  </Col>
                </div>
              </Row>
              <Row className={this.state.summarySubmission ? 'show  mb-5' : 'hide'}>
                <Col xs="12" sm="12" md="12" className="mb-5">
                  <p className="update">
                    We will send you a notification at <strong>{account.email}</strong> once your request is completed. <br />
                    Completed request will be available for download at
                    <strong> 'CMS mainframe'</strong>.
                  </p>
                </Col>
                <Divider variant="middle" />
              </Row>

              <Row id="table-wrapper" className={this.state.summarySubmission ? 'show  mb-5' : 'hide'}>
                <Col>
                  <h6 className="mb-5 .summary-2">
                    <strong>Summary</strong>
                  </h6>
                  <Table id="request" borderless className="fixed">
                    <thead>
                      <tr>
                        <th>Request ID</th>
                        <th>Requested On</th>
                        <th>Requested By</th>
                        <th>DUA Number</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>{this.state.requestId}</td>
                        <td>{this.state.todayDate}</td>
                        <td>{account.fName}</td>
                        <td>{property.dua}</td>
                      </tr>
                    </tbody>
                  </Table>
                </Col>
              </Row>

              <Row id="table-wrapper">
                <Col>
                  <h6 className={this.state.summarySubmission ? 'hide' : 'show  mb-5'}>
                    <strong>DUA # {property.dua}</strong>
                  </h6>
                  <Table borderless className="fixed">
                    <thead>
                      <tr>
                        <th>Data source</th>
                        <th>Data type</th>
                        <th>State</th>
                        <th>Year</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>{property.dataSource}</td>
                        <td>{property.dataType}</td>
                        <td>{state.toUpperCase()}</td>
                        <td>{property.year}</td>
                      </tr>
                    </tbody>
                  </Table>
                </Col>
              </Row>
              <h6 className="mb-2 search-filter">SEARCH FILTER</h6>
              <div className="filter">
                <Row id="table-search-wrapper">
                  <Col>
                    <Table responsive borderless>
                      <tbody>
                        {_.times(repeat, index => (
                          <td key={index} id={index.toString()}>
                            <h6 className="summary-criteria">{criteria.field[index].set}</h6>
                            <h6 className="summary-criteria">Field</h6>
                            <div>{criteria.field[index].name.toUpperCase()}</div>

                            <div className={criteria.operator[index] ? 'show' : 'hide'}>
                              <h6 className="summary-criteria">Operator</h6>
                              <div>{criteria.operator[index].toUpperCase()}</div>
                            </div>

                            <div className={criteria.value[index] || criteria.lookup[index] ? 'show' : 'hide'}>
                              <h6 className="summary-criteria">Value</h6>
                              <div> {criteria.value[index].toUpperCase()} </div>
                              <div> {criteria.lookup[index].toUpperCase()} </div>
                            </div>

                            <div className={criteria.date[index] ? 'show' : 'hide'}>
                              <h6 className="summary-criteria">{criteria.dateTo[index] ? 'From:' : 'Date:'}</h6>
                              <div> {criteria.date[index].toUpperCase()} </div>
                            </div>
                            <div className={criteria.dateTo[index] ? 'show' : 'hide'}>
                              <h6 className="summary-criteria">To</h6>
                              <div> {criteria.dateTo[index].toUpperCase()} </div>
                            </div>
                            {/* finder file selections */}
                            <div className={criteria.icdCode[index] ? 'show' : 'hide'}>
                              <h6 className="summary-criteria">ICD code</h6>
                              <div> {criteria.icdCode[index].toUpperCase()} </div>
                            </div>
                            <div className={criteria.finderFileName[index] ? 'show' : 'hide'}>
                              <h6 className="summary-criteria">Finder file</h6>
                              <div> {criteria.finderFileName[index].toUpperCase()} </div>
                            </div>
                            <div className={criteria.finderStartPostion[index] ? 'show' : 'hide'}>
                              <h6 className="summary-criteria">Start position</h6>
                              <div> {criteria.finderStartPostion[index].toUpperCase()} </div>
                            </div>
                            <div className={criteria.HeaderStartPosition[index] ? 'show' : 'hide'}>
                              <h6 className="summary-criteria">Header position</h6>
                              <div> {criteria.HeaderStartPosition[index].toUpperCase()} </div>
                            </div>
                          </td>
                        ))}
                      </tbody>
                    </Table>
                  </Col>
                </Row>
              </div>
              <h6 className="mb-2 search-filter">OUPTPUT</h6>
              <div className="filter">
                <Row>
                  <Col>
                    <h6 className="summary-criteria">Output type</h6>
                    <div>{output.selectedOptionOuptput} </div>
                  </Col>
                  <Col>
                    <h6 className="summary-criteria">Include dropped records</h6>
                    <div>{output.droppedRecordChecked === undefined || output.droppedRecordChecked === false ? 'N/A' : 'YES'} </div>
                  </Col>
                </Row>

                <Row>
                  <Col>
                    <h6 className="summary-criteria"> Comma delimited</h6>
                    <div>{output.commaDelimitedChecked === undefined || output.commaDelimitedChecked === false ? 'NO' : 'YES'} </div>
                  </Col>
                  <Col className={output.outputIdentifier === undefined || output.outputIdentifier === '' ? 'hide' : 'show'}>
                    <h6 className="summary-criteria">Output file identifier</h6>
                    <div>{output.outputIdentifier}</div>
                  </Col>
                </Row>

                <Row>
                  <Col className={output.selectedFields === undefined || output.selectedFields.length === 0 ? 'hide' : 'show'}>
                    <h6 className="summary-criteria">Selected fields</h6>
                    {output.selectedFields !== undefined
                      ? output.selectedFields.map(fields => <div key={fields}>{fields.toUpperCase()}</div>)
                      : 'N/A'}
                  </Col>
                  <Col className={output.requestDescription === undefined || output.requestDescription === '' ? 'hide' : 'show'}>
                    <h6 className="summary-criteria"> Request description</h6>
                    <div>{output.requestDescription}</div>
                  </Col>
                </Row>
              </div>
            </Print>
            <Row className="text-left" style={{ paddingTop: '2rem' }}>
              <Col className={this.state.hideSubmitBtn ? 'hide' : 'show  mb-5'}>
                <button className="usa-button usa-button-primary" onClick={this.handleSubmitRequest}>
                  Submit
                </button>
              </Col>
            </Row>
          </Paper>
        </PrintProvider>
      </React.Fragment>
    );
  }
}

const mapStateToProps = ({ authentication }: IRootState) => ({
  account: authentication.account
});

const mapDispatchToProps = { updateOutput, enableRequestButton, getSession };
type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;
export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Summary);
