import React from 'react';
import { Row, Col, ListGroupItem } from 'reactstrap';
import Typography from '@material-ui/core/Typography';
import * as RequestService from './new-request-service';
import ToggleDisplay from 'react-toggle-display';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import _ from 'lodash';
import { toast } from 'react-toastify';
import { connect } from 'react-redux';
import { updateOutput } from './output.reducer';
import ReactTooltip from 'react-tooltip';
import Info from '@material-ui/icons/Info';

const NCH = 'NCH NEARLINE FILE';
const availableFieldSelected = 'Select Available Fields';

interface IOutputSectionProps extends DispatchProps {
  dataSource: string;
  outPut: any;
  selectedOutput: string;
  propsControlAccordionExpand: number;
}

class OutputSection extends React.Component<IOutputSectionProps> {
  state = {
    accordionExpand: true,
    selectedOptionOuptput: '',
    selectedOutput: '',
    selectedDropedRecord: false,
    droppedRecordChecked: false,
    availableFieldChecked: [],
    selectedFieldChecked: [],
    selectedAvailableOutputField: [],
    selectedFields: [],
    selectedAvailableTempFields: [],
    selectedTempFields: [],
    availableFields: [],
    selectedAvailableFieldsIndex: [],
    selectedFieldsIndex: [],
    showFieldSelections: false,
    customViewName: '',
    showSaveViewButton: true,
    requestDescription: '',
    commaDelimitedChecked: false,
    selectedCommaDelimited: false,
    disableApplyOutputBtn: true,
    outputIdentifier: '',
    propsControlAccordionExpand: 1
  };
  static getDerivedStateFromProps(props, state) {
    if (props.propsControlAccordionExpand !== state.propsControlAccordionExpand) {
      const accordionExpand = props.propsControlAccordionExpand === 0 ? true : state.accordionExpand;
      return {
        accordionExpand,
        selectedFields: [],
        availableFieldChecked: [],
        selectedOptionOuptput: '',
        showFieldSelections: false
      };
    }
    return null;
  }
  componentDidMount() {
    this.setState({ availableFields: RequestService.getAvailableFields() });
  }

  onClickAccordion = () => {
    this.setState({ accordionExpand: !this.state.accordionExpand });
  };
  handleOutputChange = event => {
    const disableApplyOutputBtn = event.target.value === '' || event.target.value === 'Select Available Fields' ? true : false;
    this.setState({ disableApplyOutputBtn });
    const showFieldSelections = event.target.value === availableFieldSelected ? true : false;
    this.setState({ showFieldSelections });
    this.setState({ selectedOutput: event.target.value.toUpperCase() });
    this.setState({ selectedOptionOuptput: event.target.value });
    this.setState({ availableFields: RequestService.getAvailableFields() });
    this.setState({ selectedFields: [] });
    this.setState({ availableFieldChecked: [] });
    this.setState({ selectedAvailableTempFields: [] });
  };
  handleIncludeDroppedRecordSelected = () => {
    this.setState({ selectedDropedRecord: true });
  };
  onClickDroppedRecord = () => {
    this.setState({ droppedRecordChecked: !this.state.droppedRecordChecked });
  };
  handleAvailableFieldChange = (data, index) => {
    const selectedAvailableFieldsIndex = [...this.state.selectedAvailableFieldsIndex, index];
    const selectedAvailableTempFields = [...this.state.selectedAvailableTempFields];

    const indeOfSelectedField = selectedAvailableTempFields.indexOf(data);

    indeOfSelectedField === -1 ? selectedAvailableTempFields.push(data) : selectedAvailableTempFields.splice(indeOfSelectedField, 1);

    this.setState({ selectedAvailableTempFields: _.sortBy(selectedAvailableTempFields) });
    this.setState({ selectedAvailableFieldsIndex });
  };
  handleOnclickAvailableField = index => {
    // handle controlled checkbox checking
    const selection = [...this.state.availableFieldChecked];
    selection[index] = !selection[index];
    this.setState({ availableFieldChecked: selection });
  };
  handleSelectedFieldChange = (field, index) => {
    const selectedFieldsIndex = [...this.state.selectedFieldsIndex, index];
    const selectedTempFields = [...this.state.selectedTempFields];

    const indeOfSelectedField = selectedTempFields.indexOf(field);

    indeOfSelectedField === -1 ? selectedTempFields.push(field) : selectedTempFields.splice(indeOfSelectedField, 1);
    this.setState({ selectedTempFields: _.sortBy(selectedTempFields) });
    this.setState({ selectedFieldsIndex });
  };
  handleOnclickSelectedField = index => {
    // handle controlled checkbox checking
    const selection = [...this.state.selectedFieldChecked];
    selection[index] = !selection[index];
    this.setState({ selectedFieldChecked: selection });
  };

  handleAvailableFieldClearCheckBox = index => {
    const selection = [];
    selection[index] = false;
    this.setState({ availableFieldChecked: selection });
  };
  handleSelectedFieldClearCheckBox = index => {
    const selection = [];
    selection[index] = false;
    this.setState({ selectedFieldChecked: selection });
  };
  handleAddField = () => {
    const selectedFields = [...this.state.selectedFields, ...this.state.selectedAvailableTempFields];
    // remove empty element
    if (selectedFields[0] === '') {
      selectedFields.splice(0, 1);
    }
    this.setState({ selectedFields: _.uniq(selectedFields) });

    const availableFields = [...this.state.availableFields];
    // remove selected fields
    const difference = _.differenceWith(availableFields, _.uniq(selectedFields), _.isEqual);

    this.setState({ availableFields: _.sortBy(difference) });
    const disableApplyOutputBtn = availableFields.length > 0 ? false : true;
    this.setState({ disableApplyOutputBtn });
    this.setState({ selectedAvailableTempFields: [] });
    this.handleAvailableFieldCheckBox();
  };
  handleRemoveField = () => {
    const selectedTempFields = [...this.state.selectedTempFields];
    const availableFields = [...this.state.availableFields];
    const merged = _.union(availableFields, selectedTempFields);
    this.setState({ availableFields: _.sortBy(merged) });

    const selectedFields = [...this.state.selectedFields];
    // remove selected fields
    const difference = _.differenceWith(selectedFields, _.uniq(selectedTempFields), _.isEqual);

    this.setState({ selectedFields: _.sortBy(difference) });
    const disableApplyOutputBtn = selectedFields.length === 1 ? true : false;
    this.setState({ disableApplyOutputBtn });
    this.setState({ selectedTempFields: [] });
    this.handleSelectedFieldCheckBox();
  };
  handleSelectedFieldCheckBox = () => {
    for (const index of this.state.selectedFieldsIndex) {
      this.handleSelectedFieldClearCheckBox(index);
    }
  };
  handleAvailableFieldCheckBox = () => {
    for (const index of this.state.selectedAvailableFieldsIndex) {
      this.handleAvailableFieldClearCheckBox(index);
    }
  };
  handleInputCustomViewNameChange = event => {
    this.setState({ customViewName: event.target.value.toUpperCase() });
    this.setState({ showSaveViewButton: event.target.value === '' ? true : false });
  };

  // TODO: will be implmented when MQ is up and runing
  handleSaveView = () => {
    toast.success(this.state.customViewName + ' custom view saved successfully');
  };

  handleRequestDescriptionChange = event => {
    this.setState({ requestDescription: event.target.value.toUpperCase() });
  };

  handleInputOutputIdentifierChange = event => {
    this.setState({ outputIdentifier: event.target.value.toUpperCase() });
  };

  handleCommaDelimitedSelected = () => {
    this.setState({ selectedCommaDelimited: true });
  };
  onClickCommaDelimited = () => {
    this.setState({ commaDelimitedChecked: !this.state.commaDelimitedChecked });
  };
  handleApplyOutput = () => {
    // pass output value to the store
    this.props.updateOutput(this.state);
    this.setState({ disableApplyOutputBtn: true });
    this.clearSelections();
  };
  clearSelections = () => {
    this.setState({ selectedOptionOuptput: '' });
    this.setState({ showFieldSelections: false });
    this.setState({ droppedRecordChecked: false });

    this.setState({ selectedFieldChecked: [] });

    this.setState({ selectedFields: [] });

    this.setState({ customViewName: '' });

    this.setState({ outputIdentifier: '' });

    this.setState({ requestDescription: '' });

    this.setState({ commaDelimitedChecked: false });
  };
  render() {
    const selectedOutput =
      this.state.selectedOutput === '' || (this.props.selectedOutput === '' || this.props.selectedOutput === undefined)
        ? 'NO'
        : this.state.selectedOutput === ''
          ? this.props.selectedOutput
          : this.state.selectedOutput;

    return (
      <React.Fragment>
        <ul className="usa-accordion-bordered mt-5">
          <li>
            <button
              onClick={this.onClickAccordion}
              className="usa-accordion-button"
              aria-expanded={!this.state.accordionExpand}
              aria-controls="ouput-criteria"
            >
              <span>Output:</span>
              <span className="accordion-text ml-1">
                <strong> {selectedOutput} </strong> ouput selected
              </span>
            </button>
            <div id="ouput-criteria" className="usa-accordion-content" aria-hidden={this.state.accordionExpand}>
              <Row className="mb-4">
                <Col xs="4" sm="4" className="mt-1" style={{ zindex: 10000, position: 'static' }}>
                  <label htmlFor="ouputOptions">
                    <Typography align="left" variant="h6">
                      Select an output type:
                    </Typography>
                  </label>
                  <select
                    className="ds-c-field"
                    name="ouputOptions"
                    id="ouputOptions"
                    onChange={this.handleOutputChange}
                    value={this.state.selectedOptionOuptput}
                  >
                    <option value="">- output -</option>
                    {this.props.outPut.map(data => (
                      <option key={data.description} value={data.description}>
                        {data.description}
                      </option>
                    ))}
                  </select>
                </Col>

                <Col xs="6" sm="6" style={{ marginTop: '20px' }} className={this.state.selectedOutput === '' ? 'disabled' : ''}>
                  <fieldset className={this.props.dataSource === NCH ? 'show fieldset-custom' : 'hide'}>
                    <input
                      style={{ paddingTop: '-5px' }}
                      className="input-choice ds-c-choice--small"
                      id="output-record"
                      type="checkbox"
                      name="output-record"
                      value="output-record"
                      onChange={this.handleIncludeDroppedRecordSelected}
                      checked={this.state.droppedRecordChecked}
                      onClick={this.onClickDroppedRecord}
                    />
                    <label className="custom-label-text" htmlFor="output-record">
                      Include dropped records: <Info data-tip data-for="droppedRecordsInfo" className="pl-1" />
                      <ReactTooltip id="droppedRecordsInfo" type="dark" effect="float">
                        <span>
                          OPTIONAL: Selecting include dropped Records will provide you with a separate output file for the dropped records.
                        </span>
                      </ReactTooltip>
                    </label>
                  </fieldset>
                </Col>
              </Row>

              <ToggleDisplay show={this.state.showFieldSelections}>
                <Row className="mb-4">
                  <Col xs="4" sm="4">
                    <ul className="list-group">
                      <ListGroupItem className="field-list">Available fields</ListGroupItem>
                      {this.state.availableFields.map((data, index) => (
                        <li className="list-item" key={index}>
                          <input
                            className="ds-c-choice"
                            id={data}
                            type="checkbox"
                            name={data}
                            value={data}
                            checked={this.state.availableFieldChecked[index]}
                            onChange={() => {
                              this.handleAvailableFieldChange(data, index);
                            }}
                            onClick={() => {
                              this.handleOnclickAvailableField(index);
                            }}
                          />
                          <label htmlFor={data}>{data}</label>
                        </li>
                      ))}
                    </ul>
                  </Col>

                  <Col xs="1" sm="1" className="arrow">
                    <div className="left-rigt-arrow mb-3" onClick={this.handleAddField}>
                      <FontAwesomeIcon title="Add field" color="gray" size="2x" icon="angle-right" />
                    </div>
                    <div className="left-rigt-arrow" onClick={this.handleRemoveField}>
                      <FontAwesomeIcon title="Remove field" color="gray" size="2x" icon="angle-left" />
                    </div>
                  </Col>

                  <Col xs="4" sm="4">
                    <ul className="list-group">
                      <ListGroupItem className="field-list">Selected fields</ListGroupItem>
                      {this.state.selectedFields.map((data, index) => (
                        <li className="list-item" key={index}>
                          <input
                            className="ds-c-choice"
                            id={data}
                            type="checkbox"
                            name={data}
                            value={data}
                            checked={this.state.selectedFieldChecked[index]}
                            onChange={() => {
                              this.handleSelectedFieldChange(data, index);
                            }}
                            onClick={() => {
                              this.handleOnclickSelectedField(index);
                            }}
                          />
                          <label htmlFor={data}>{data}</label>
                        </li>
                      ))}
                    </ul>
                  </Col>
                </Row>
              </ToggleDisplay>
              <Row className="mt-4">
                <Col xs="4" sm="4">
                  <label htmlFor="customViewName">
                    <Typography variant="h6">
                      Custom view name:
                      <Info data-tip data-for="viewName" className="pl-1" />
                    </Typography>
                    <ReactTooltip id="viewName" type="dark" effect="float">
                      <span>OPTIONAL: Retain the request you have created.</span>
                    </ReactTooltip>
                  </label>
                  <input
                    className="ds-c-field"
                    id="customViewName"
                    name="customViewName"
                    value={this.state.customViewName}
                    type="text"
                    onChange={this.handleInputCustomViewNameChange}
                  />
                </Col>
                <Col xs="6" sm="6" style={{ marginTop: '18px' }}>
                  <button
                    style={{ maxHeight: '43px', paddingTop: '10px' }}
                    className="usa-button usa-button-secondary"
                    disabled={this.state.showSaveViewButton}
                    onClick={this.handleSaveView}
                  >
                    Save view
                  </button>
                </Col>
              </Row>
              <Row className="mt-4">
                <Col xs="4" sm="4">
                  <label htmlFor="identifier">
                    <Typography variant="h6">
                      Enter an identifier for your output file:
                      <Info data-tip data-for="identifier" className="pl-1" />
                    </Typography>
                    <ReactTooltip id="identifier" type="dark" effect="float">
                      <span>
                        OPTIONAL: Up to seven alphanumeric characters that will be incorporated in the file name to allow for easy
                        identification of the output data.
                      </span>
                    </ReactTooltip>
                  </label>
                  <input
                    className="ds-c-field"
                    id="identifier"
                    name="identifier"
                    value={this.state.outputIdentifier}
                    type="text"
                    onChange={this.handleInputOutputIdentifierChange}
                    maxLength={7}
                  />
                </Col>
              </Row>
              <Row className="mt-4">
                <Col xs="4" sm="4">
                  <label htmlFor="RequestDescription">
                    <Typography variant="h6">
                      Enter a description for your request:
                      <Info data-tip data-for="description" className="pl-1" />
                    </Typography>
                    <ReactTooltip id="description" type="dark" effect="float">
                      <span>OPTIONAL: A description that is meaningful to you for identifying this request.</span>
                    </ReactTooltip>
                  </label>
                  <input
                    className="ds-c-field"
                    id="RequestDescription"
                    name="RequestDescription"
                    value={this.state.requestDescription}
                    type="text"
                    onChange={this.handleRequestDescriptionChange}
                  />
                </Col>
              </Row>

              <Row className="mt-4">
                <Col xs="4" sm="4">
                  <fieldset>
                    <input
                      id="comma-delimited"
                      type="checkbox"
                      name="comma-delimited"
                      value="comma-delimited"
                      onChange={this.handleCommaDelimitedSelected}
                      checked={this.state.commaDelimitedChecked}
                      onClick={this.onClickCommaDelimited}
                    />
                    <label className="custom-label-text" htmlFor="comma-delimited">
                      Comma delimited:
                      <Info data-tip data-for="commadelimited" className="pl-1" />
                      <ReactTooltip id="commadelimited" type="dark" effect="float">
                        <span>OPTIONAL: Select for comma delimited output.</span>
                      </ReactTooltip>
                    </label>
                  </fieldset>
                </Col>
              </Row>
              <Row className="mt-4">
                <Col xs="4" sm="4">
                  <button
                    className="usa-button usa-button-secondary"
                    disabled={this.state.disableApplyOutputBtn}
                    onClick={this.handleApplyOutput}
                  >
                    Apply ouput
                  </button>
                </Col>
              </Row>
            </div>
          </li>
        </ul>
      </React.Fragment>
    );
  }
}
const mapDispatchToProps = { updateOutput };

type DispatchProps = typeof mapDispatchToProps;
export default connect(
  null,
  mapDispatchToProps
)(OutputSection);
