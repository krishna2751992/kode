import './request.scss';
// tslint:disable-next-line:no-submodule-imports
import 'react-accessible-accordion/dist/fancy-example.css';
// tslint:disable-next-line:no-submodule-imports
import 'react-day-picker/lib/style.css';
import React from 'react';
import _ from 'lodash';
import { Row, Col } from 'reactstrap';
import Paper from '@material-ui/core/Paper';
import ArrowBack from '@material-ui/icons/ArrowBack';
import ToggleDisplay from 'react-toggle-display';
import DuaAssignedTable from '../../shared/util/tables/dua-assigned-table';
import AlertDialog from '../../shared/util/dialog/alert-dialog';
import Chip from '@material-ui/core/Chip';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import { Redirect } from 'react-router';
import { RouteComponentProps } from 'react-router-dom';
import { connect } from 'react-redux';
import { disableRequestButton, enableManageRequestButton } from './../../shared/layout/header/nav-bar.reducer';
import * as RequestService from './new-request-service';
import { Typography } from '@material-ui/core';
import Info from '@material-ui/icons/Info';
import DeleteForeverIcon from '@material-ui/icons/DeleteForever';
import ReactTooltip from 'react-tooltip';
import Repeat from 'react-repeat-component';
// tslint:disable-next-line:no-submodule-imports
import DayPickerInput from 'react-day-picker/DayPickerInput';
import DateUtils from 'react-day-picker';
import moment from 'moment';
import { toast } from 'react-toastify';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Grid from '@material-ui/core/Grid';
import { DragDropContext } from 'react-beautiful-dnd';
import styled from 'styled-components';
import Column from './column';
import OutputSection from './output-section';
import Divider from '@material-ui/core/Divider';
import { updateOutput } from '../../modules/new-request/output.reducer';
import { Choice, ChoiceList } from '@cmsgov/design-system-core';
import { enableRequestButton } from '../../shared/layout/header/nav-bar.reducer';
import http from '../../shared/service/http-service';
import LoadingOverlay from 'react-loading-overlay';
import { FadeLoader } from 'react-spinners';

const theme = createMuiTheme({
  overrides: {
    MuiChip: {
      root: {
        borderRadius: '5px'
      },
      label: {
        paddingLeft: '3px'
      }
    }
  },
  palette: {
    background: {
      default: '#fff'
    }
  },
  typography: {
    useNextVariants: true
  }
});
let selectedDate;
let dataTypeId;
let fileName;
let showICD;
const operators = '';
let operatorsData = [];
let columnId;
let datasourceId;
let encryptionSwitchLetter;
let selectedDuaForDataSource;
let dua = '';
let openAlert = false;
const Container = styled.div`
  display: flex;
`;

interface INewRequestProps extends StateProps, DispatchProps, RouteComponentProps<{ selectedDua: string }> {
  showPropertySection: boolean;
  dataSource: any;
}
class NewRequest extends React.Component<INewRequestProps> {
  private readonly DATE_FIELD = 'Date';
  private readonly DATE_RANGE = 'RG';
  private readonly HICN = 'HICN';
  state = {
    selectedDataSource: '',
    selectedDataType: '',
    selectedState: '',
    selectedYear: [],
    selectedField: { columnID: '', name: '', set: '' },
    showPropertySection: false,
    value: '',
    back: false,
    selectedDua: '',
    isDisabled: true,
    selectedOption: '',
    selectedOptionDataSource: '',
    selectedOptionDataType: '',
    selectedOptionState: '',
    selectedOptionYear: [],
    selectFinderField: '',
    selectOperatorField: '',
    selectedOptionOperator: '',
    disableSection: true,
    disableApplyFilterBtn: true,
    finderFileChecked: false,
    operatorChecked: false,
    accordionCriteriaSearchExpand: true,
    selectedOptionIcdCode: '',
    selectedOptionLookUpValue: '',
    inputCriteria: '',
    dateInputCriteria: '',
    toDateInputCriteria: '',
    finderInputFileName: '',
    finderInputStartPosition: '',
    finderInputHeaderStartPosition: '',
    showCriteriaSelection: false,
    accordionSearchCriteriaSelectionExpand: [],
    selectionCriteria: [],
    showFilterAddedMessage: false,
    showErrorMessage: false,
    numberOfSelectedCriteria: 1,
    selectedCriteriaField: [] as any,
    selectedCriteriaOperator: [],
    selectedCriteriaLookUpValue: [],
    selectedCriteriaInputsValue: [],
    selectedCriteriaIcdCode: [],
    selectedCriteriaFinderInputFileName: [],
    selectedCriteriaFinderInputStartPosition: [],
    selectedCriteriaFinderInputHeaderStartPosition: [],
    selectedCriteriaDateInputValue: [],
    selectedCriteriaToDateInputValue: [],
    openCriteriaDialog: false,
    criteriaSetData: RequestService.getCriteriaData(),
    criteriaSetAppliedMessage: '  ',
    inputYearChecked: [],
    accordionExpand: true,
    selectedOperatorField: '',
    finderChecked: false,
    selectedOptionField: '',
    deletedFieldIndex: null,
    propsAccordionExpand: false,
    dataSource: [],
    userData: [],
    dataType: [],
    datasourceMain: [],
    stateOption: {},
    showState: false,
    stateData: [],
    yearData: [],
    searchCriteriaData: [],
    columRuleData: [],
    showFinderFileOption: false,
    lookUpData: [],
    outPutTypeData: [],
    fileNotFoundError: false,
    loading: true,
    dataDescriptionCode: ''
  };
  componentDidMount() {
    this.props.disableRequestButton();
    this.props.enableManageRequestButton();
    if (this.props.match != null) {
      this.setState({ selectedDua: this.props.match.params.selectedDua });
    }
    if (
      this.props.location.state !== undefined &&
      this.props.location.state.property !== undefined &&
      this.props.location.state.criteria !== undefined &&
      this.props.location.state.redirectedFromSummary === true
    ) {
      this.setSelectedData();
    }
    this.setIputYearCheckBoxState();
    this.getDataSource();
    this.getOutPutType();
  }
  getDataSource = () => {
    if (this.props.match !== null && this.props.match !== undefined) {
      selectedDuaForDataSource = this.props.match.params.selectedDua;
      this.setState({ selectedDua: this.props.match.params.selectedDua });
    }
    if (this.props.location.state.dataSource !== undefined && this.props.location.state.dataSource.length !== 0) {
      const encryptionSwitchFiltered = _.filter(
        this.props.location.state.dataSource,
        item => String(item.duaNumber) === selectedDuaForDataSource
      );
      encryptionSwitchLetter = encryptionSwitchFiltered === [] ? '' : encryptionSwitchFiltered[0].encryptionSwitch;
    }

    http
      .get(`api/get-datasource/` + selectedDuaForDataSource)
      .then(response => {
        this.setState({ datasourceMain: response.data, loading: false });
      })
      .catch(error => {
        this.setState({ datasourceMain: [], loading: false });
        console.error('error api call to api/get-datasource/ ' + error);
      });
  };
  getDataType = async val => {
    datasourceId = val;
    const { data: dataType } = await http.get(`api/get-datatypes/` + selectedDuaForDataSource + '/' + encryptionSwitchLetter + '/' + val);
    this.setState({ dataType });
    const { data: stateOption } = await http.get('api/get-datasource-rule/' + val);
    this.setState({ stateOption });
    const { data: stateData } = await http.get('api/get-states/' + selectedDuaForDataSource + '/' + val);
    this.setState({ stateData });
    this.setState({ showState: stateOption.selectState });
  };
  getYearData = async val => {
    if (this.state.showState) {
      const { data: yearData } = await http.get(
        'api/get-years/' + selectedDuaForDataSource + '/' + datasourceId + '/' + dataTypeId + '/' + val
      );
      this.setState({ yearData });
    } else {
      const { data: yearData } = await http.get('api/get-years-no-statecode/' + selectedDuaForDataSource + '/' + datasourceId + '/' + val);
      this.setState({ yearData });
    }
    const { data: searchCriteriaData } = await http.get('api/get-criteria-fields/' + datasourceId + '/' + dataTypeId);
    this.setState({ searchCriteriaData });
  };

  getColumRules = async () => {
    const { data: columRuleData } = await http.get('api/get-column-rules/' + datasourceId + '/' + dataTypeId + '/' + columnId);
    this.setState({ columRuleData });
    this.setState({ showFinderFileOption: columRuleData.enableFinder });
    this.setState({ lookUpData: columRuleData.lookups });
    showICD = columRuleData.enableIcd;
    operatorsData = _.split(columRuleData.operators, ',');
  };
  getOutPutType = async () => {
    const { data: outPutTypeData } = await http.get('api/get-output-types');
    this.setState({ outPutTypeData });
  };
  handleFileCheck = async event => {
    fileName = event.target.value;
    const { data: fileNameData } = await http.get('api/check-finder-file/ ', fileName);
    this.setState({ fileNotFoundError: !fileNameData });
  };
  componentWillUnmount() {
    this.props.updateOutput({} as any);
  }
  setSelectedData() {
    // set data when user redirecred from summary
    const { property } = this.props.location.state;
    const { criteria } = this.props.location.state;
    const { criteriaSetData } = this.props.location.state;
    this.setState({ criteriaSetData: { ...criteriaSetData } });
    this.setState({ selectedDataSource: property.dataSource });
    this.setState({ selectedDataType: property.dataType });
    this.setState({ selectedState: property.state });
    this.setState({ selectedYear: [...property.year] });

    this.setState({ selectedCriteriaField: [...criteria.field] });
    this.setState({ selectedCriteriaOperator: [...criteria.operator] });
    this.setState({ selectedCriteriaInputsValue: [...criteria.value] });
    this.setState({ selectedCriteriaLookUpValue: [...criteria.lookup] });

    this.setState({ selectedCriteriaDateInputValue: [...criteria.date] });
    this.setState({ selectedCriteriaToDateInputValue: [...criteria.dateTo] });
    this.setState({ selectedCriteriaIcdCode: [...criteria.icdCode] });

    this.setState({ selectedCriteriaFinderInputFileName: [...criteria.finderFileName] });
    this.setState({ selectedCriteriaFinderInputStartPosition: [...criteria.finderStartPostion] });
    this.setState({
      selectedCriteriaFinderInputHeaderStartPosition: [...criteria.HeaderStartPosition]
    });

    this.setState({ numberOfSelectedCriteria: criteria.operator.length + 1 });
    // set accordion for proper state
    const count = criteria.operator.length + 1;
    const accordionSearchCriteriaSelectionExpand = [];
    for (let i = 0; i < count; i++) {
      accordionSearchCriteriaSelectionExpand[i] = true;
    }
    this.setState({ accordionSearchCriteriaSelectionExpand });
  }
  setIputYearCheckBoxState = () => {
    const inputYearChecked = [];
    for (let i = 0; i < RequestService.getYears().length; i++) {
      inputYearChecked[i] = false;
    }
    this.setState({ inputYearChecked });
  };
  handleDataSourceChange = event => {
    const dataSourceSelected = JSON.parse(event.target.value);
    this.getDataType(dataSourceSelected.dataSourceId);
    this.clearInputYearSelections(dataSourceSelected.name);
    this.setState({ selectedDataSource: dataSourceSelected.name });
    this.setState({ selectedOptionDataSource: dataSourceSelected.name });
    this.setState({ selectedDataType: '' });
    this.setIputYearCheckBoxState();
    this.setState({ selectedOptionDataType: '' });
    if (event.target.value !== this.state.selectedDataSource && this.state.selectedDataSource !== '') {
      this.setState({ propsAccordionExpand: true });
    } else {
      this.setState({ propsAccordionExpand: false });
    }

    this.clearAllSelections();
  };
  handleDataTypeChange = event => {
    const dataTypeIds = JSON.parse(event.target.value);
    dataTypeId = dataTypeIds.dataTypeID;
    this.getYearData(dataTypeId);
    this.clearInputYearSelections(dataTypeIds.name);
    this.setState({ selectedDataType: dataTypeIds.name });
    this.setState({ selectedOptionDataType: dataTypeIds.name });
    this.setIputYearCheckBoxState();
    this.clearAllSelections();
    this.setState({ propsAccordionExpand: false });
  };
  clearAllSelections = () => {
    this.setState({ selectedState: '' });
    this.setState({ selectedOptionState: '' });
    this.setState({ selectedYear: [] });
    this.setState({ selectedOptionYear: [] });
    this.setState({ disableSection: true });
    this.setState({ showFilterAddedMessage: false });
    this.setState({ showErrorMessage: false });
    this.setState({ selectedCriteriaField: [] });
    this.setState({ selectedCriteriaOperator: [] });
    this.setState({ selectedCriteriaInputsValue: [] });
    this.setState({ selectedCriteriaLookUpValue: [] });
    this.setState({ selectedCriteriaDateInputValue: [] });
    this.setState({ selectedCriteriaToDateInputValue: [] });
    this.setState({ selectedCriteriaIcdCode: [] });
    this.setState({ selectedCriteriaFinderInputFileName: [] });
    this.setState({ selectedCriteriaFinderInputStartPosition: [] });
    this.setState({ selectedCriteriaFinderInputHeaderStartPosition: [] });
    this.setState({ numberOfSelectedCriteria: 1 });
    this.props.updateOutput({} as any);
    if (this.state.accordionExpand === false) {
      this.setState({ accordionExpand: true });
    }

    let criterias = this.state.criteriaSetData.criterias;
    const leftColumn = this.state.criteriaSetData.columns['column-1'];
    const rightColumn = this.state.criteriaSetData.columns['column-2'];
    leftColumn.criteriaIds = [];
    rightColumn.criteriaIds = [];
    leftColumn.rowId = [];
    rightColumn.rowId = [];
    criterias = {};

    const criteriaSetData = {
      criterias,
      columns: { ...this.state.criteriaSetData.columns, 'column-1': leftColumn, 'column-2': rightColumn },
      columnOrder: ['column-1', 'column-2']
    };

    this.setState({ criteriaSetData });
  };
  clearInputYearSelections = value => {
    if (value === '') {
      this.setState({ inputYearChecked: [] });
      this.setState({ selectedYear: [] });
    }
  };
  handleStateChange = event => {
    const stateDatas = JSON.parse(event.target.value);
    this.getYearData(stateDatas.stateCode);
    this.clearInputYearSelections(stateDatas.description);
    this.setState({ selectedState: stateDatas.description });
    this.setState({ selectedOptionState: stateDatas.description });
    if (this.state.selectedYear !== []) {
      this.setState({ disableSection: true });
    }
    this.setIputYearCheckBoxState();
  };
  handleYearChange = event => {
    const selectedYears = [...this.state.selectedYear];
    const indeOfSelectedYear = selectedYears.indexOf(event.target.value);
    indeOfSelectedYear === -1 ? selectedYears.push(event.target.value) : selectedYears.splice(indeOfSelectedYear, 1);

    this.setState({ selectedYear: selectedYears });
    this.setState({ selectedOptionYear: selectedYears });
    if (this.state.showState && this.state.selectedState === '') {
      this.setState({ disableSection: true });
    } else {
      this.setState({ disableSection: false });
    }
    if (selectedYears.length === 0 && this.state.accordionExpand === false) {
      this.setState({ accordionExpand: true });
    }
  };
  handleOnclickInputYear = index => {
    const selection = [...this.state.inputYearChecked];
    selection[index] = !selection[index];
    this.setState({ inputYearChecked: selection });
  };

  handleFieldChange = event => {
    const selectedField = JSON.parse(event.target.value);
    columnId = selectedField.columnID;

    this.setState({ showFilterAddedMessage: false });
    this.setState({ showErrorMessage: false });
    this.clearSearchCriteriaSection();
    this.setState({ selectedField });
    this.setState({ selectedOptionField: selectedField.name });
    this.setState({ lookUpData: [] });
    this.getColumRules();
  };

  handleFinderFileSelected = event => {
    this.setState({ selectFinderField: event.target.value });
    this.setState({ selectOperatorField: '' });
  };
  handleOperatorSelected = event => {
    this.setState({ selectOperatorField: event.target.value });
    this.setState({ selectFinderField: '' });
  };
  onClickFinderFile = () => {
    this.setState({ operatorChecked: false });
    this.setState({ finderFileChecked: !this.state.finderFileChecked });
  };
  onClickOperator = () => {
    this.setState({ finderFileChecked: false });
    this.setState({ operatorChecked: !this.state.operatorChecked });
  };

  clearSearchCriteriaSection = () => {
    this.setState({ operatorChecked: false });
    this.setState({ finderFileChecked: false });
    this.clearCriteriaSelection(false);
  };

  handleOperatorChange = event => {
    this.setState({ selectedOptionOperator: event.target.value });
    this.clearSelection();
  };
  handleLookUpChange = event => {
    this.setState({ selectedOptionLookUpValue: event.target.value });
    this.showApplyFilterButton(event);
  };
  handleIcdCodeChange = event => {
    this.setState({ selectedOptionIcdCode: event.target.value });
  };
  handleInputCriteriaChange = event => {
    this.setState({ inputCriteria: event.target.value });
    this.showApplyFilterButton(event);
  };
  handleChangeFinderInputFileName = event => {
    const finderFileName = event.target.value;
    if (finderFileName.length === 0) {
      this.setState({ fileNotFoundError: false });
    }
    this.setState({ finderInputFileName: event.target.value });
  };
  handleChangeFinderInputStartPostion = event => {
    this.setState({ finderInputStartPosition: event.target.value });
    this.showApplyFilterButton(event);
  };
  handleChangeFinderInputHeaderStart = event => {
    this.setState({ finderInputHeaderStartPosition: event.target.value });
  };
  handleOnDayChange = day => {
    selectedDate = day;
    const date = moment(day).format('YYYY/MM/DD');
    this.setState({ dateInputCriteria: date.toString() });
    if (this.state.selectedOptionOperator !== this.DATE_RANGE) {
      this.showApplyFilterButton(event);
    }
  };

  handleOnDayChangeTo = day => {
    this.setState({ showErrorMessage: false });
    const date = moment(day).format('YYYY/MM/DD');
    this.setState({ toDateInputCriteria: date.toString() });
    if (DateUtils.DateUtils.isDayBefore(day, selectedDate)) {
      this.setState({ showErrorMessage: true });
    } else {
      this.showApplyFilterButton(event);
    }
  };

  handleCloseAlertDialog = () => {
    openAlert = false;
  };
  handleBackButton = () => {
    this.setState({ back: true });
  };

  onClickSearchCriteriaSelectionAccordion = index => {
    const selection = [...this.state.accordionSearchCriteriaSelectionExpand];
    selection[index] = !selection[index];
    this.setState({ accordionSearchCriteriaSelectionExpand: selection });
  };
  handleApplyCriteria = () => {
    this.setState({ showCriteriaSelection: true });
    this.setState({ disableApplyFilterBtn: true });
    this.setState({
      accordionSearchCriteriaSelectionExpand: [...this.state.accordionSearchCriteriaSelectionExpand, true]
    });
    // unique lable for multiple selection of same field
    // get and use deleted index if not null
    const rowId =
      this.state.deletedFieldIndex !== null && this.state.deletedFieldIndex !== undefined
        ? this.state.deletedFieldIndex
        : this.state.numberOfSelectedCriteria;

    const selectedField = { ...this.state.selectedField, rowId, set: '' };

    this.setState({
      selectedCriteriaField: [...this.state.selectedCriteriaField, selectedField]
    });
    this.setState({
      selectedCriteriaOperator: [...this.state.selectedCriteriaOperator, this.state.selectedOptionOperator]
    });

    this.setState({
      selectedCriteriaInputsValue: [...this.state.selectedCriteriaInputsValue, this.state.inputCriteria]
    });
    this.setState({
      selectedCriteriaDateInputValue: [...this.state.selectedCriteriaDateInputValue, this.state.dateInputCriteria]
    });
    this.setState({
      selectedCriteriaToDateInputValue: [...this.state.selectedCriteriaToDateInputValue, this.state.toDateInputCriteria]
    });
    this.setState({
      selectedCriteriaLookUpValue: [...this.state.selectedCriteriaLookUpValue, this.state.selectedOptionLookUpValue]
    });

    this.setState({
      selectedCriteriaIcdCode: [...this.state.selectedCriteriaIcdCode, this.state.selectedOptionIcdCode]
    });

    this.setState({
      selectedCriteriaFinderInputFileName: [...this.state.selectedCriteriaFinderInputFileName, this.state.finderInputFileName]
    });

    this.setState({
      selectedCriteriaFinderInputStartPosition: [
        ...this.state.selectedCriteriaFinderInputStartPosition,
        this.state.finderInputStartPosition
      ]
    });

    this.setState({
      selectedCriteriaFinderInputHeaderStartPosition: [
        ...this.state.selectedCriteriaFinderInputHeaderStartPosition,
        this.state.finderInputHeaderStartPosition
      ]
    });

    this.setState({ showFilterAddedMessage: true });

    // set criteria data for drag n drop
    // get and use deleted index if not null
    const selectedFields = this.state.selectedField;
    const criteriasId = '' + this.state.selectedField.columnID + rowId;
    const content = this.state.selectedField.name + '(' + rowId + ')';
    const selectedCriterias = { id: criteriasId, content };
    const leftColumn = this.state.criteriaSetData.columns['column-1'];
    const rightColumn = this.state.criteriaSetData.columns['column-2'];
    if (rightColumn.criteriaIds.length > 0) {
      const allCriteriaIds = [...leftColumn.criteriaIds, ...rightColumn.criteriaIds];
      const allRowIds = [...leftColumn.rowId, ...rightColumn.rowId];
      // move all filtered criteriaIds and rowIds to  set1
      rightColumn.criteriaIds = [];
      rightColumn.rowId = [];
      leftColumn.criteriaIds = [];
      leftColumn.rowId = [];
      leftColumn.criteriaIds.push(...allCriteriaIds);
      leftColumn.rowId.push(...allRowIds);
    }

    // remove duplicate
    const leftColumnCriteria = leftColumn.criteriaIds;
    const indexOfCriteria = leftColumnCriteria.indexOf(parseInt(criteriasId, 10));
    if (indexOfCriteria === -1) {
      leftColumn.criteriaIds.push(criteriasId);
      leftColumn.rowId.push(rowId);
    }
    const criteriaSetData = {
      criterias: { ...this.state.criteriaSetData.criterias, [criteriasId]: selectedCriterias },
      columns: { ...this.state.criteriaSetData.columns, 'column-1': leftColumn, 'column-2': rightColumn },
      columnOrder: ['column-1', 'column-2']
    };

    this.setState({ criteriaSetData });

    this.setState({
      numberOfSelectedCriteria: this.state.numberOfSelectedCriteria + 1
    });

    this.setState({ deletedFieldIndex: null });

    this.clearCriteriaSelection(true);
  };

  handleDeleteSelectedField = (fieldId, rowId, index) => {
    let selectedCriteriaField = [...this.state.selectedCriteriaField];
    this.setState({
      deletedFieldIndex: rowId
    });

    // filter deleted field
    const deletedField = _.filter(selectedCriteriaField, item => {
      return item.fieldId === fieldId && item.rowId === rowId;
    });

    // remove deleted field
    selectedCriteriaField = _.differenceWith(selectedCriteriaField, deletedField, _.isEqual);

    // remove set criteria, if any field is removed

    const updatedCriteriaField = selectedCriteriaField.map(o => ({ ...o, set: '' }));

    this.setState({ selectedCriteriaField: updatedCriteriaField });

    const selectedCriteriaOperator = this.state.selectedCriteriaOperator;
    this.state.selectedCriteriaOperator.splice(index, 1);
    this.setState({
      selectedCriteriaOperator
    });

    const selectedCriteriaInputsValue = this.state.selectedCriteriaInputsValue;
    this.state.selectedCriteriaInputsValue.splice(index, 1);
    this.setState({
      selectedCriteriaInputsValue
    });

    const selectedCriteriaDateInputValue = this.state.selectedCriteriaDateInputValue;
    this.state.selectedCriteriaDateInputValue.splice(index, 1);
    this.setState({
      selectedCriteriaDateInputValue
    });

    const selectedCriteriaToDateInputValue = this.state.selectedCriteriaToDateInputValue;
    this.state.selectedCriteriaToDateInputValue.splice(index, 1);
    this.setState({
      selectedCriteriaToDateInputValue
    });

    const selectedCriteriaLookUpValue = this.state.selectedCriteriaLookUpValue;
    this.state.selectedCriteriaLookUpValue.splice(index, 1);
    this.setState({
      selectedCriteriaLookUpValue
    });

    // finder file

    const selectedCriteriaIcdCode = this.state.selectedCriteriaIcdCode;
    this.state.selectedCriteriaIcdCode.splice(index, 1);
    this.setState({
      selectedCriteriaIcdCode
    });

    const selectedCriteriaFinderInputFileName = this.state.selectedCriteriaFinderInputFileName;
    this.state.selectedCriteriaFinderInputFileName.splice(index, 1);
    this.setState({
      selectedCriteriaFinderInputFileName
    });

    const selectedCriteriaFinderInputStartPosition = this.state.selectedCriteriaFinderInputStartPosition;
    this.state.selectedCriteriaFinderInputStartPosition.splice(index, 1);
    this.setState({
      selectedCriteriaFinderInputStartPosition
    });

    const selectedCriteriaFinderInputHeaderStartPosition = this.state.selectedCriteriaFinderInputHeaderStartPosition;
    this.state.selectedCriteriaFinderInputHeaderStartPosition.splice(index, 1);
    this.setState({
      selectedCriteriaFinderInputHeaderStartPosition
    });

    // remove deleted field from criteria set data
    this.handleDeletedCriteriaSet(fieldId, rowId, index);

    this.setState({
      showFilterAddedMessage: false
    });
    this.setState({
      showErrorMessage: false
    });

    this.setState({
      numberOfSelectedCriteria: this.state.numberOfSelectedCriteria - 1
    });
    // no more selected field.so clear
    if (this.state.numberOfSelectedCriteria === 1) {
      this.setState({ deletedFieldIndex: null });
      this.setState({ selectedCriteriaField: [] });
      this.setState({ numberOfSelectedCriteria: 1 });
    }
    this.clearCriteriaSelection(true);
  };

  handleDeletedCriteriaSet(fieldId, rowId, index) {
    const removedIndex = '' + fieldId + rowId;
    const columnOrder = this.state.criteriaSetData.columnOrder;
    // remove deleted Field from criterias  array
    const criterias = _.omit(this.state.criteriaSetData.criterias, removedIndex);

    const set1Column = this.state.criteriaSetData.columns['column-1'];
    const set2Column = this.state.criteriaSetData.columns['column-2'];

    const allcriteriaIds = [...set1Column.criteriaIds, ...set2Column.criteriaIds];
    // remove deleted field
    const criteriaIds = allcriteriaIds.filter(item => item !== removedIndex.toString());

    // remove deleted rowId from set1Column
    const indexOfRowId1 = set1Column.rowId.indexOf(rowId);
    if (indexOfRowId1 !== -1) {
      set1Column.rowId.splice(indexOfRowId1, 1);
    }

    // remove deleted rowId from set2Column
    const indexOfRowId2 = set2Column.rowId.indexOf(rowId);
    if (indexOfRowId2 !== -1) {
      set2Column.rowId.splice(indexOfRowId2, 1);
    }

    // move all filtered criteriaIds to  set1
    set1Column.criteriaIds = [];
    set1Column.criteriaIds.push(...criteriaIds);

    // remove all field dragged to set 2
    set2Column.criteriaIds = [];

    // set modified data
    const criteriaSetData = { columnOrder, columns: { ['column-1']: set1Column, ['column-2']: set2Column }, criterias };

    this.setState({ criteriaSetData });
  }

  handleSave = () => {
    toast.success('Request saved successfully');
  };
  handleCreateCriteriaSet = () => {
    this.setState({ openCriteriaDialog: true });
    this.setState({ showFilterAddedMessage: false });
  };
  handleApplyCriteriaSet = () => {
    let updatedCriteriaField = [] as any;
    const selectedCriteriaField = [...this.state.selectedCriteriaField];
    const criteriaSetData = { ...this.state.criteriaSetData };
    const leftColumn = this.state.criteriaSetData.columns['column-1'].rowId;
    const rightColumn = this.state.criteriaSetData.columns['column-2'].rowId;

    // check if field is dragged to set1 and set set label
    for (const rowId of leftColumn) {
      const set1Criteria = _.filter(selectedCriteriaField, item => {
        return item.rowId === rowId;
      });
      set1Criteria[0]['set'] = 'SET 1';
      updatedCriteriaField = [...updatedCriteriaField, ...set1Criteria];
    }

    for (const rowId of rightColumn) {
      const set2Criteria = _.filter(selectedCriteriaField, item => {
        return item.rowId === rowId;
      });
      set2Criteria[0]['set'] = 'SET 2';
      updatedCriteriaField = [...updatedCriteriaField, ...set2Criteria];
    }

    // order of the selected field matters so sort them
    const sortedCriteriaField = _.orderBy(updatedCriteriaField, ['rowId'], ['asc']);

    this.setState({ selectedCriteriaField: sortedCriteriaField });

    this.setState({ openCriteriaDialog: false });
    this.setState({ showFilterAddedMessage: true });
    this.setState({ criteriaSetAppliedMessage: ' SET ' });
  };

  onCloseCriteriaDialog = () => {
    this.setState({ openCriteriaDialog: false });
  };

  showApplyFilterButton(event: any) {
    this.setState({ disableApplyFilterBtn: event.target.value === '' ? true : false });
  }

  clearCriteriaSelection(clearField) {
    if (clearField) {
      this.setState({ selectedOptionField: '' });
    }
    this.setState({ disableApplyFilterBtn: true });
    this.setState({ finderFileChecked: false });
    this.setState({ operatorChecked: false });
    this.setState({ finderChecked: false });
    this.setState({ selectFinderField: '' });
    this.setState({ selectedOperatorField: '' });
    this.setState({ selectedOptionOperator: '' });
    this.clearSelection();
  }
  clearSelection() {
    this.setState({ inputCriteria: '' });
    this.setState({ dateInputCriteria: '' });
    this.setState({ toDateInputCriteria: '' });
    this.setState({ selectedOptionIcdCode: '' });
    this.setState({ finderInputFileName: '' });
    this.setState({ finderInputStartPosition: '' });
    this.setState({ finderInputHeaderStartPosition: '' });
    this.setState({ selectedOptionLookUpValue: '' });
  }
  onClickAccordion = () => {
    this.setState({ accordionExpand: !this.state.accordionExpand });
  };
  handleOperatorSelection = event => {
    this.setState({ selectedOperatorField: event.target.value });
    if (event.target.value === 'finder') {
      this.setState({ finderChecked: true });
      this.setState({ operatorChecked: false });
    } else {
      this.setState({ operatorChecked: true });
      this.setState({ finderChecked: false });
    }
  };

  onDragEnd = result => {
    const { destination, source, draggableId } = result;

    if (!destination) {
      return;
    }

    if (destination.droppableId === source.droppableId && destination.index === source.index) {
      return;
    }

    const start = this.state.criteriaSetData.columns[source.droppableId];
    const finish = this.state.criteriaSetData.columns[destination.droppableId];

    if (start === finish) {
      const newCriteriaIds = Array.from(start.criteriaIds);
      newCriteriaIds.splice(source.index, 1);
      newCriteriaIds.splice(destination.index, 0, draggableId);

      const newColumn = {
        ...start,
        criteriaIds: newCriteriaIds
      };

      const criteriaSetData = {
        ...this.state.criteriaSetData,
        columns: { ...this.state.criteriaSetData.columns, [newColumn.id]: newColumn }
      };

      this.setState({ criteriaSetData });

      return;
    }

    // Moving from one list to another
    const startCriteriaIds = Array.from(start.criteriaIds);
    const startRowIds = Array.from(start.rowId);
    const rowIds = Array.from(start.rowId);
    startCriteriaIds.splice(source.index, 1);
    startRowIds.splice(source.index, 1);
    const newStart = { ...start, criteriaIds: startCriteriaIds, rowId: startRowIds };

    const finishRowId = _.differenceWith(rowIds, startRowIds, _.isEqual);

    const finishCriteriaIds = Array.from(finish.criteriaIds);
    const finishRowdsIds = Array.from(finish.rowId);
    finishCriteriaIds.splice(destination.index, 0, draggableId);
    finishRowdsIds.push(...finishRowId);

    const newFinish = { ...finish, criteriaIds: finishCriteriaIds, rowId: finishRowdsIds };

    const newCriteriaSetData = {
      ...this.state.criteriaSetData,
      columns: {
        ...this.state.criteriaSetData.columns,
        [newStart.id]: newStart,
        [newFinish.id]: newFinish
      }
    };
    this.setState({ criteriaSetData: newCriteriaSetData });
  };

  handleShowSummary = () => {
    const property = {
      dataDescriptionCode: this.state.dataDescriptionCode,
      dataSource: this.state.selectedDataSource,
      dataType: this.state.selectedDataType,
      state: this.state.selectedState,
      year: this.state.selectedYear,
      dua
    };
    const criteria = {
      field: this.state.selectedCriteriaField,
      operator: this.state.selectedCriteriaOperator,
      value: this.state.selectedCriteriaInputsValue,
      lookup: this.state.selectedCriteriaLookUpValue,
      date: this.state.selectedCriteriaDateInputValue,
      dateTo: this.state.selectedCriteriaToDateInputValue,
      icdCode: this.state.selectedCriteriaIcdCode,
      finderFileName: this.state.selectedCriteriaFinderInputFileName,
      finderStartPostion: this.state.selectedCriteriaFinderInputStartPosition,
      HeaderStartPosition: this.state.selectedCriteriaFinderInputHeaderStartPosition
    };
    this.props.history.push({
      pathname: '/summary',
      state: {
        property,
        criteria,
        criteriaSetData: this.state.criteriaSetData,
        output: this.props.output
      }
    });
  };

  render() {
    const dataSourceDropdown =
      this.state.datasourceMain.length === 0
        ? this.props.location.state.duaDataSource === undefined
          ? []
          : this.props.location.state.duaDataSource
        : this.state.datasourceMain;
    const { loading } = this.state;
    const repeat = this.state.numberOfSelectedCriteria - 1;
    // get output section data from redux store
    const { output } = this.props;
    let tooglePropertySection = false;
    const { showYearDropdown, rowMargin, yearAlign, yearSelectAlign } = this.yearSelectionAlign();
    const mTop = showYearDropdown ? rowMargin : {};
    tooglePropertySection = this.toogleSelection(tooglePropertySection);
    this.getSelectedDua();
    if (this.state.back) {
      return <Redirect to={{ pathname: '/' }} />;
    }

    return (
      <React.Fragment>
        <AlertDialog
          alertOpen={openAlert}
          onClose={this.handleCloseAlertDialog}
          fromHome={false}
          selectedDua={this.state.selectedDua}
          dataSource={this.state.dataSource}
        />
        <Dialog maxWidth="md" open={this.state.openCriteriaDialog} onClose={this.onCloseCriteriaDialog} aria-labelledby="dialog-title">
          <DialogTitle id="dialog-title" className="mb-3 mt-2">
            <Grid container>
              <Grid item xs={10}>
                <h1 className="ds-h2" id="dialog-title">
                  Search criteria set
                </h1>
              </Grid>
              <Grid id="dialogClose" item xs={2} className="pl-5" style={{ marginTop: '-10px' }}>
                <button
                  className="ds-c-button ds-c-button--transparent ds-c-dialog__close"
                  aria-label="Close modal dialog"
                  onClick={this.onCloseCriteriaDialog}
                >
                  Close
                </button>
              </Grid>
            </Grid>
          </DialogTitle>
          <DialogContent>
            <DialogContentText className="mt-0">
              <div className="body-text-l">By default, all criteria are created in Set 1. To add one or more filters in set 2, drag</div>
              <div className="body-text-l mb-2">and drop the field names and click apply.</div>
              <div className="body-text-s">
                Each set will be considered as a separate request.Set 2 will only be used if the system does not find
              </div>
              <div className="body-text-s mb-4">data using criteria Set 1.</div>
              <div style={{ marginLeft: '-8px' }}>
                <DragDropContext onDragEnd={this.onDragEnd}>
                  <Container>
                    {this.state.criteriaSetData.columnOrder.map(columnIds => {
                      const column = this.state.criteriaSetData.columns[columnIds];
                      const criterias = column.criteriaIds.map(criteriaId => this.state.criteriaSetData.criterias[criteriaId]);
                      return <Column key={column.id} column={column} criterias={criterias} />;
                    })}
                  </Container>
                </DragDropContext>
              </div>
            </DialogContentText>
          </DialogContent>

          <div className="criteria-set-dialog mb-4 mt-2" style={{ marginLeft: '25px' }}>
            <button onClick={this.handleApplyCriteriaSet} className="ds-c-button ds-c-button--primary mr-5">
              Apply
            </button>
            <span id="dialogClose">
              <button
                className="ds-c-button ds-c-button--transparent ds-c-dialog__close"
                aria-label="Close modal dialog"
                onClick={this.onCloseCriteriaDialog}
              >
                Cancel
              </button>
            </span>
          </div>
        </Dialog>
        <ToggleDisplay show={!tooglePropertySection}>
          <div className="mb-4" style={{ display: 'inline-block' }}>
            <Typography variant="h6" style={{ color: '#0071bb', cursor: 'pointer' }} onClick={this.handleBackButton}>
              <ArrowBack /> <u>Back</u>
            </Typography>
          </div>
          <Paper className="pt-4 pb-4" style={{ width: '80%' }}>
            <div className="pr-4 pl-4">
              <DuaAssignedTable title={'Select a DUA to create a request:'} tableOptions={false} fromNewRequestComponent />
            </div>
          </Paper>
        </ToggleDisplay>

        <ToggleDisplay show={tooglePropertySection}>
          <Row className="mr-3">
            <Col className="pr-0" xs="auto" sm="6" md="4">
              <Paper style={{ backgroundColor: '#f1f1f1', height: '100%' }}>
                <Typography variant="h6" className="mb-4 pt-2 back-arrow" onClick={this.handleBackButton}>
                  <ArrowBack /> <u>Back</u>
                </Typography>

                <MuiThemeProvider theme={theme}>
                  <ToggleDisplay show={tooglePropertySection}>
                    <div className="mb-5 mt-4 pt-4 position-relative">
                      <Typography variant="h6">DUA</Typography>
                      <span className="selection">{dua}</span>

                      <ToggleDisplay show={this.state.selectedDataSource !== ''}>
                        <Typography className="mt-4" variant="h6">
                          Data Source
                        </Typography>
                        <span className="selection">{this.state.selectedDataSource.toUpperCase()}</span>
                      </ToggleDisplay>
                      <ToggleDisplay show={this.state.selectedDataType !== ''}>
                        <Typography className="mt-4" variant="h6">
                          Data Type
                        </Typography>
                        <span className="selection">{this.state.selectedDataType.toUpperCase()}</span>
                      </ToggleDisplay>

                      <ToggleDisplay show={this.state.selectedState !== ''}>
                        <Typography className="mt-4" variant="h6">
                          State
                        </Typography>
                        <span className="selection">{this.state.selectedState.toUpperCase()}</span>
                      </ToggleDisplay>
                      <ToggleDisplay show={this.state.selectedYear.length !== 0}>
                        <Typography className=" mt-4" variant="h6">
                          Year
                        </Typography>
                        {this.state.selectedYear.map(year => (
                          <div key={year}>
                            <span className="selection">{year}</span>
                          </div>
                        ))}
                      </ToggleDisplay>
                    </div>

                    {/*Criteria section  */}

                    <span className={this.state.numberOfSelectedCriteria === 1 ? 'hide' : 'show mb-3'}>
                      <Divider variant="middle" />
                      <div className="position-relative mb-3">
                        <Typography className="mt-4" variant="h6">
                          <strong> Search Criteria </strong>
                        </Typography>
                      </div>

                      <div className="wrapper mb-5">
                        <div id="field-criteria">
                          <ul className="usa-accordion-bordered">
                            {_.times(repeat, i => (
                              <li key={i} id={i.toString()}>
                                <DeleteForeverIcon
                                  style={{ cursor: 'pointer' }}
                                  onClick={() => {
                                    this.handleDeleteSelectedField(
                                      this.state.selectedCriteriaField[i].columnID,
                                      this.state.selectedCriteriaField[i].rowId,
                                      i
                                    );
                                  }}
                                />
                                <button
                                  onClick={() => {
                                    this.onClickSearchCriteriaSelectionAccordion(i);
                                  }}
                                  className="usa-accordion-button pt-2"
                                  aria-expanded={!this.state.accordionSearchCriteriaSelectionExpand[i]}
                                  aria-controls="searchCriteriaSelection"
                                >
                                  <span>
                                    <Typography variant="subtitle1" style={{ color: 'black' }}>
                                      <strong>{this.state.selectedCriteriaField[i].set}</strong>
                                    </Typography>
                                    <span style={{ color: 'black' }}>Field:</span>
                                    {this.state.selectedCriteriaField[i].name.toUpperCase() +
                                      '(' +
                                      this.state.selectedCriteriaField[i].rowId +
                                      ')'}
                                  </span>
                                </button>
                                <div
                                  id="searchCriteriaSelection"
                                  className="usa-accordion-content accordion-content-background"
                                  aria-hidden={this.state.accordionSearchCriteriaSelectionExpand[i]}
                                >
                                  <div className="verticalLine">
                                    <span className={this.state.selectedCriteriaOperator[i] ? 'show' : 'hide'}>
                                      <Typography variant="h6">
                                        Operator:
                                        <span> {this.state.selectedCriteriaOperator[i].toUpperCase()} </span>
                                      </Typography>
                                    </span>
                                    <span
                                      className={
                                        this.state.selectedCriteriaInputsValue[i] || this.state.selectedCriteriaLookUpValue[i]
                                          ? 'show'
                                          : 'hide'
                                      }
                                    >
                                      <Typography variant="h6">
                                        Value:
                                        <span> {this.state.selectedCriteriaInputsValue[i].toUpperCase()} </span>
                                        <span> {this.state.selectedCriteriaLookUpValue[i].toUpperCase()} </span>
                                      </Typography>
                                    </span>

                                    <span className={this.state.selectedCriteriaDateInputValue[i] ? 'show' : 'hide'}>
                                      <Typography variant="h6">
                                        {this.state.selectedCriteriaToDateInputValue[i] ? 'Date From:' : 'Date:'}
                                        <span> {this.state.selectedCriteriaDateInputValue[i]} </span>
                                      </Typography>
                                    </span>

                                    <span className={this.state.selectedCriteriaToDateInputValue[i] ? 'show' : 'hide'}>
                                      <Typography variant="h6">
                                        Date To:
                                        <span> {this.state.selectedCriteriaToDateInputValue[i]} </span>
                                      </Typography>
                                    </span>
                                    {/* finder file selections */}
                                    <span className={this.state.selectedCriteriaIcdCode[i] ? 'show' : 'hide'}>
                                      <Typography variant="h6">
                                        ICD code:
                                        <span> {this.state.selectedCriteriaIcdCode[i].toUpperCase()} </span>
                                      </Typography>
                                    </span>
                                    <span className={this.state.selectedCriteriaFinderInputFileName[i] ? 'show' : 'hide'}>
                                      <Typography variant="h6">
                                        Finder file:
                                        <span> {this.state.selectedCriteriaFinderInputFileName[i].toUpperCase()} </span>
                                      </Typography>
                                    </span>
                                    <span className={this.state.selectedCriteriaFinderInputStartPosition[i] ? 'show' : 'hide'}>
                                      <Typography variant="h6">
                                        Start position:
                                        <span> {this.state.selectedCriteriaFinderInputStartPosition[i].toUpperCase()} </span>
                                      </Typography>
                                    </span>
                                    <span className={this.state.selectedCriteriaFinderInputHeaderStartPosition[i] ? 'show' : 'hide'}>
                                      <Typography variant="h6">
                                        Header start position:
                                        <span> {this.state.selectedCriteriaFinderInputHeaderStartPosition[i].toUpperCase()} </span>
                                      </Typography>
                                    </span>
                                  </div>
                                </div>
                              </li>
                            ))}
                          </ul>
                        </div>
                      </div>
                    </span>
                  </ToggleDisplay>

                  <span className={output.selectedOptionOuptput === undefined ? 'hide' : 'show mb-3'}>
                    <Divider variant="middle" />
                  </span>

                  {/*Ouput section  */}

                  <ToggleDisplay hide={output.selectedOptionOuptput === undefined}>
                    <div className="position-relative">
                      <Typography className="mt-4" variant="h6">
                        <strong>Output</strong>
                      </Typography>
                    </div>
                    <div className="mb-5 position-relative">
                      <ToggleDisplay className="mb-4" hide={output.selectedOutput === undefined || output.selectedOutput === ''}>
                        <Typography className="mt-4" variant="h6">
                          Output Type:
                        </Typography>
                        <span className="selection">{output.selectedOutput}</span>
                      </ToggleDisplay>

                      <ToggleDisplay className="mb-4" hide={output.selectedFields === undefined || output.selectedFields.length === 0}>
                        <Typography className="mt-4" variant="h6">
                          Selected Fields:
                        </Typography>

                        {output.selectedFields !== undefined
                          ? output.selectedFields.map(fields => (
                              <div key={fields}>
                                <span className="selection">{fields.toUpperCase()}</span>
                              </div>
                            ))
                          : ''}
                      </ToggleDisplay>

                      <ToggleDisplay
                        className="mb-4"
                        hide={output.droppedRecordChecked === undefined || output.droppedRecordChecked === false}
                      >
                        <Typography className="mt-4" variant="h6">
                          Include dropped records:
                        </Typography>
                        <span className="selection">YES</span>
                      </ToggleDisplay>

                      <ToggleDisplay className="mb-4" hide={output.customViewName === undefined || output.customViewName === ''}>
                        <Typography className="mt-4" variant="h6">
                          Custom view name:
                        </Typography>
                        <span className="selection">{output.customViewName}</span>
                      </ToggleDisplay>

                      <ToggleDisplay className="mb-4" hide={output.outputIdentifier === undefined || output.outputIdentifier === ''}>
                        <Typography className="mt-4" variant="h6">
                          Output file identifier:
                        </Typography>
                        <span className="selection">{output.outputIdentifier}</span>
                      </ToggleDisplay>

                      <ToggleDisplay className="mb-4" hide={output.requestDescription === undefined || output.requestDescription === ''}>
                        <Typography className="mt-4" variant="h6">
                          Request description:
                        </Typography>
                        <span className="selection">{output.requestDescription}</span>
                      </ToggleDisplay>

                      <ToggleDisplay
                        className="mb-4"
                        hide={output.commaDelimitedChecked === undefined || output.commaDelimitedChecked === false}
                      >
                        <Typography className="mt-4" variant="h6">
                          Comma delimited:
                        </Typography>
                        <span className="selection">YES</span>
                      </ToggleDisplay>
                    </div>
                  </ToggleDisplay>
                </MuiThemeProvider>
              </Paper>
            </Col>
            {/* Right COL */}
            <Col xs="auto" sm="6" md="8">
              <Paper style={{ padding: '5%' }}>
                <ToggleDisplay show={tooglePropertySection}>
                  <h5 style={{ marginTop: '-4%', paddingBottom: '1%' }}> Request Creation</h5>
                  <Row className="mt-4">
                    <Col xs="6">
                      <label htmlFor="dataSourceOptions">
                        <Typography align="left" variant="h6">
                          Select a data source for the DUA:
                        </Typography>
                      </label>
                      <LoadingOverlay
                        active={loading}
                        styles={{ overlay: base => ({ ...base, background: 'rgba(0, 0, 0, 0.3)' }) }}
                        spinner={<FadeLoader color={'#4A90E2'} />}
                      >
                        <select
                          className="ds-c-field"
                          name="dataSourceOptions"
                          id="dataSourceOptions"
                          onChange={this.handleDataSourceChange}
                        >
                          <option value="">- Data Source -</option>
                          {dataSourceDropdown.map(data => (
                            <option key={data.dataSourceId} value={JSON.stringify(data)}>
                              {data.name}
                            </option>
                          ))}
                        </select>
                      </LoadingOverlay>
                    </Col>
                    <Col xs="6" className="mb-3">
                      <div className={this.state.selectedDataSource === '' ? 'disabled' : ''}>
                        <label htmlFor="dataTypeOptions">
                          <Typography align="left" variant="h6">
                            Select a data type for your request:
                          </Typography>
                        </label>
                        <select className="ds-c-field" name="dataTypeOptions" id="dataTypeOptions" onChange={this.handleDataTypeChange}>
                          <option value="">- Data Type -</option>
                          {this.state.dataType.map(data => (
                            <option key={data.dataTypeID} value={JSON.stringify(data)}>
                              {data.name}
                            </option>
                          ))}
                        </select>
                      </div>
                    </Col>
                  </Row>
                  <Row>
                    <Col xs="6" className={this.state.selectedDataType !== '' && this.state.showState ? 'show mb-5' : 'hide'}>
                      <label htmlFor="stateOptions">
                        <Typography align="left" variant="h6" className="mt-5">
                          Select state for the data source:
                        </Typography>
                      </label>
                      <select className="ds-c-field" name="stateOptions" id="stateOptions" onChange={this.handleStateChange}>
                        <option value="">- States -</option>
                        {this.state.stateData.map(data => (
                          <option key={data.stateCode} value={JSON.stringify(data)}>
                            {data.description}
                          </option>
                        ))}
                      </select>
                    </Col>

                    <Col className={yearAlign}>
                      <ToggleDisplay show={showYearDropdown}>
                        <label htmlFor="years">
                          <Typography align="left" variant="h6" className="mt-5">
                            Select one or more years for your request:
                          </Typography>
                        </label>
                        <fieldset className={yearSelectAlign}>
                          <legend className="usa-sr-only">Select Year</legend>
                          <ul className="usa-unstyled-list">
                            <span className="ml-5">
                              {' '}
                              {'year'} {','} {'update date'}{' '}
                            </span>

                            {this.state.yearData.map((data, index) => (
                              <li key={index}>
                                <input
                                  className="ds-c-choice"
                                  id={data.year}
                                  type="checkbox"
                                  name={data.year}
                                  value={data.year}
                                  onChange={this.handleYearChange}
                                  checked={this.state.inputYearChecked[index]}
                                  onClick={() => {
                                    this.handleOnclickInputYear(index);
                                  }}
                                />

                                <label htmlFor={data.year}>
                                  <Typography variant="h6">
                                    {data.year} {','} {data.updateDate}
                                  </Typography>
                                </label>
                              </li>
                            ))}
                          </ul>
                        </fieldset>
                      </ToggleDisplay>
                    </Col>
                  </Row>

                  <Row style={mTop}>
                    <Col>
                      {/*
                      Criteria section  */}
                      <div id="accordion-wrapper" className={this.state.selectedYear.length === 0 ? 'disable' : ''}>
                        <ul className="usa-accordion-bordered mt-5">
                          <li>
                            <button
                              onClick={this.onClickAccordion}
                              className="usa-accordion-button"
                              aria-expanded={!this.state.accordionExpand}
                              aria-controls="search-criteria"
                            >
                              <span>Search Criteria:</span>
                              <span className="accordion-text ml-1">
                                <strong>{this.state.numberOfSelectedCriteria - 1} criteria </strong> applied
                              </span>
                            </button>
                            <div id="search-criteria" className="usa-accordion-content" aria-hidden={this.state.accordionExpand}>
                              <Row className="mb-4">
                                <Col xs="6" sm="6" className="mt-1" style={{ zindex: 10000, position: 'static' }}>
                                  <label htmlFor="fieldOptions">
                                    <Typography align="left" variant="h6">
                                      Select a field to add search filter:
                                    </Typography>
                                  </label>
                                  <select
                                    className="ds-c-field"
                                    name="fieldOptions"
                                    id="fieldOptions"
                                    onChange={this.handleFieldChange}
                                    value={this.state.selectedOptionField}
                                  >
                                    <option value="">
                                      {this.state.selectedOptionField === '' ? '- Field -' : this.state.selectedOptionField}
                                    </option>
                                    {this.state.searchCriteriaData.map(data => (
                                      <option key={data.columnID} value={JSON.stringify(data)}>
                                        {data.name}
                                      </option>
                                    ))}
                                  </select>
                                </Col>
                                <Col
                                  xs="6"
                                  sm="6"
                                  style={{ marginTop: '20px', zindex: 10000, position: 'static' }}
                                  className={this.state.selectedField.name === '' ? 'disabled' : ''}
                                >
                                  <ChoiceList
                                    choices={[
                                      {
                                        label: (
                                          <Typography style={{ marginTop: '-15px' }} variant="h6">
                                            User input file
                                          </Typography>
                                        ),
                                        value: 'finder',
                                        checked: this.state.finderChecked,
                                        disabled: !this.state.showFinderFileOption
                                      },
                                      {
                                        label: (
                                          <Typography style={{ marginTop: '-12px' }} variant="h6">
                                            Search operator
                                          </Typography>
                                        ),
                                        value: 'operator',
                                        checked: this.state.operatorChecked
                                      }
                                    ]}
                                    className="fieldset-custom ds-u-margin-top--0"
                                    label={<Typography variant="srOnly">Select operator:</Typography>}
                                    name="choices_field"
                                    onChange={this.handleOperatorSelection}
                                  />
                                </Col>
                              </Row>

                              <ToggleDisplay show={showICD === 'Y' && this.state.operatorChecked === false}>
                                <Row className="mb-1">
                                  <Col xs="6" sm="6">
                                    <label htmlFor="icdCode">
                                      <Typography align="left" variant="h6">
                                        Select ICD code:
                                      </Typography>
                                    </label>
                                    <select
                                      className="ds-c-field"
                                      name="icdCode"
                                      id="icdCode"
                                      onChange={this.handleIcdCodeChange}
                                      value={this.state.selectedOptionIcdCode}
                                    >
                                      <option value="">- ICD Code -</option>
                                      {RequestService.getIcdCode().map(data => (
                                        <option key={data.icdCode} value={data.icdCode}>
                                          {data.icdCode}
                                        </option>
                                      ))}
                                    </select>
                                  </Col>
                                </Row>
                              </ToggleDisplay>

                              {/* Finder file input */}

                              <ToggleDisplay
                                show={
                                  (this.state.selectedOptionOperator !== '' &&
                                    this.state.selectedOperatorField === 'finder' &&
                                    this.state.selectedOptionIcdCode !== '') ||
                                  this.state.selectedOperatorField === 'finder'
                                }
                              >
                                <Row className="mb-5 mt-5">
                                  <Col xs="4" sm="4" lg="6">
                                    <label htmlFor="finderFileName" className="custom-label-text">
                                      Filename: <Info data-tip data-for="findeFileInfo" className="pl-1" />
                                      <ReactTooltip id="findeFileInfo" type="dark" effect="float">
                                        <span>Mainframe data set name of the user input file</span>
                                      </ReactTooltip>
                                    </label>
                                    <input
                                      className="ds-c-field"
                                      id="finderFileName"
                                      name="finderfile"
                                      type="text"
                                      value={this.state.finderInputFileName}
                                      onBlur={this.handleFileCheck}
                                      onChange={this.handleChangeFinderInputFileName}
                                    />
                                    <label style={{ marginTop: '0px', fontSize: 'bold' }}>
                                      <strong>
                                        *RECORD LIMIT <br /> Non-HICAN finder files: 150,000 records <br />
                                        HICAN finder files: 3 million records
                                      </strong>
                                    </label>
                                  </Col>

                                  <Col xs="4" sm="4" lg="3">
                                    <label htmlFor="fileStartPostion" className="custom-label-text">
                                      Start position:
                                      <Info data-tip data-for="startPosition" className="pl-1" />
                                      <ReactTooltip id="startPosition" type="dark" effect="float">
                                        <span>Beginning location of the data in the finder file</span>
                                      </ReactTooltip>
                                    </label>
                                    <input
                                      className="ds-c-field"
                                      id="fileStartPostion"
                                      name="startPostion"
                                      type="number"
                                      value={this.state.finderInputStartPosition}
                                      onChange={this.handleChangeFinderInputStartPostion}
                                    />
                                  </Col>

                                  <Col
                                    xs="4"
                                    sm="4"
                                    lg="3"
                                    className={this.state.selectedOptionField.includes(this.HICN) ? '' : 'disabled'}
                                  >
                                    <label htmlFor="headerStartPostion" className="custom-label-text">
                                      Header start position:
                                      <Info data-tip data-for="headerPosition" className="pl-1" />
                                      <ReactTooltip id="headerPosition" type="dark" effect="float">
                                        <span>Beginning location of a 30-byte user defined area, such as an internal control number</span>
                                      </ReactTooltip>
                                    </label>
                                    <input
                                      className="ds-c-field"
                                      id="headerStartPostion"
                                      name="headerStartPostion"
                                      type="number"
                                      value={this.state.finderInputHeaderStartPosition}
                                      onChange={this.handleChangeFinderInputHeaderStart}
                                    />
                                  </Col>
                                </Row>
                              </ToggleDisplay>
                              <ToggleDisplay show={this.state.selectedOperatorField === 'operator' && this.state.selectedField.name !== ''}>
                                <Row className="mb-5">
                                  <Col xs="6" sm="6" lg="6" className="mt-1" style={{ zindex: 10000, position: 'static' }}>
                                    <label htmlFor="operators">
                                      <Typography variant="h6">Select an operator:</Typography>
                                    </label>
                                    <select
                                      className="ds-c-field"
                                      name="operators"
                                      id="operators"
                                      onChange={this.handleOperatorChange}
                                      value={this.state.selectedOptionOperator}
                                    >
                                      <option value="">- Operator -</option>
                                      {operatorsData.map(data => (
                                        <option key={data} value={data}>
                                          {data}
                                        </option>
                                      ))}
                                    </select>
                                  </Col>
                                  <Col
                                    xs="auto"
                                    sm="auto"
                                    lg="6"
                                    className={
                                      this.state.selectedOptionOperator !== '' && this.state.lookUpData.length !== 0 ? 'show mt-1' : 'hide'
                                    }
                                  >
                                    <label htmlFor="criteriaLookupValue">
                                      <Typography variant="h6">Select lookup value:</Typography>
                                    </label>
                                    <select
                                      className="ds-c-field"
                                      name="criteriaLookupValue"
                                      id="criteriaLookupValue"
                                      onChange={this.handleLookUpChange}
                                      value={this.state.selectedOptionLookUpValue}
                                    >
                                      <option value="">- Lookup value -</option>
                                      {this.state.lookUpData.map(data => (
                                        <option key={data.lookupID} value={data.lookupText}>
                                          {data.lookupText}
                                        </option>
                                      ))}
                                    </select>
                                  </Col>
                                  <Col
                                    xs="auto"
                                    sm="auto"
                                    lg="6"
                                    className={
                                      this.state.selectedOptionOperator !== '' &&
                                      !this.state.selectedOptionField.includes(this.DATE_FIELD) &&
                                      this.state.lookUpData.length === 0
                                        ? 'show mt-1'
                                        : 'hide'
                                    }
                                  >
                                    <label htmlFor="criteriaValue">
                                      <Typography variant="h6">Enter a value:</Typography>
                                    </label>
                                    <input
                                      className="ds-c-field"
                                      id="criteriaValue"
                                      name="criteriaValue"
                                      value={this.state.inputCriteria}
                                      type="text"
                                      onChange={this.handleInputCriteriaChange}
                                    />
                                  </Col>
                                  <Col
                                    xs="3"
                                    sm="3"
                                    lg={this.state.selectedOptionOperator === this.DATE_RANGE ? '3' : '6'}
                                    className={
                                      this.state.selectedOptionOperator !== '' && this.state.selectedOptionField.includes(this.DATE_FIELD)
                                        ? 'show '
                                        : 'hide'
                                    }
                                  >
                                    <label htmlFor="criteriaDate" aria-label="criteriaDate">
                                      <Typography variant="h6">
                                        {this.state.selectedOptionOperator === this.DATE_RANGE ? 'Date From:' : 'Enter Date:'}
                                      </Typography>
                                    </label>
                                    <DayPickerInput
                                      aria-describedby="criteriaDate"
                                      placeholder="YYYY/M/D"
                                      format="YYYY/MM/DD"
                                      onDayChange={day => this.handleOnDayChange(day)}
                                      value={this.state.dateInputCriteria}
                                    />
                                  </Col>

                                  <span
                                    style={{ marginTop: '45px', marginLeft: '-10px', marginRight: '-10px' }}
                                    className={
                                      this.state.selectedOptionOperator === this.DATE_RANGE &&
                                      this.state.selectedField.name.includes(this.DATE_FIELD)
                                        ? 'show '
                                        : 'hide'
                                    }
                                  >
                                    {''}—{''}
                                  </span>

                                  <Col
                                    xs="3"
                                    sm="3"
                                    lg="3"
                                    className={
                                      this.state.selectedOptionOperator === this.DATE_RANGE &&
                                      this.state.selectedField.name.includes(this.DATE_FIELD)
                                        ? 'show '
                                        : 'hide'
                                    }
                                  >
                                    <label htmlFor="criteriaDateTo" aria-labelledby="criteriaDateTo">
                                      <Typography variant="h6">Date To:</Typography>
                                    </label>

                                    <DayPickerInput
                                      aria-labelledby="criteriaDateTo"
                                      placeholder="YYYY/M/D"
                                      format="YYYY/MM/DD"
                                      onDayChange={day => this.handleOnDayChangeTo(day)}
                                      value={this.state.toDateInputCriteria}
                                    />
                                  </Col>
                                </Row>
                              </ToggleDisplay>

                              <Row id="criteria" className="mb-0 mt-4">
                                <Col xs="4" sm="4" md="4">
                                  <button
                                    className="usa-button usa-button-secondary"
                                    disabled={this.state.disableApplyFilterBtn}
                                    onClick={this.handleApplyCriteria}
                                  >
                                    Apply criteria
                                  </button>
                                </Col>

                                <Col
                                  xs="8"
                                  sm="8"
                                  md="8"
                                  className={this.state.showFilterAddedMessage ? 'ds-c-alert ds-c-alert--success' : 'hide'}
                                >
                                  <div className="ds-c-alert__body">
                                    <h3 className="ds-c-alert__heading">
                                      Criteria
                                      {this.state.criteriaSetAppliedMessage}
                                      applied
                                    </h3>
                                    <p className="ds-c-alert__text">
                                      Criteria
                                      {this.state.criteriaSetAppliedMessage}
                                      applied successfully.{' '}
                                    </p>
                                  </div>
                                </Col>
                                <Col xs="8" sm="8" md="8" className={this.state.showErrorMessage ? 'ds-c-alert ds-c-alert--error' : 'hide'}>
                                  <div className="ds-c-alert__body">
                                    <h3 className="ds-c-alert__heading">Invalid date range</h3>
                                    <p className="ds-c-alert__text">'To' date selected must be after 'from' date</p>
                                  </div>
                                </Col>

                                <Col
                                  xs="8"
                                  sm="8"
                                  md="8"
                                  className={this.state.fileNotFoundError && fileName.length > 0 ? 'ds-c-alert ds-c-alert--error' : 'hide'}
                                >
                                  <div className="ds-c-alert__body">
                                    <h3 className="ds-c-alert__heading">Finder file not found</h3>
                                    <p className="ds-c-alert__text">Please enter the correct finder file</p>
                                  </div>
                                </Col>
                              </Row>
                              <Row id="criteria">
                                <Col className={this.state.numberOfSelectedCriteria >= 3 ? 'show pr-1 ml-1 mt-3' : 'hide'}>
                                  <a onClick={this.handleCreateCriteriaSet}>Create search criteria set</a>
                                </Col>
                              </Row>
                            </div>
                          </li>
                        </ul>
                      </div>
                      {/*
                     output section  */}
                      <div id="accordion-wrapper" className={this.state.selectedCriteriaField.length === 0 ? 'disable' : ''}>
                        <OutputSection
                          propsControlAccordionExpand={this.state.selectedCriteriaField.length}
                          dataSource={this.state.selectedDataSource}
                          outPut={this.state.outPutTypeData}
                          selectedOutput={output.selectedOptionOuptput}
                        />
                      </div>
                    </Col>
                  </Row>

                  <Row className="mt-5 mb-5 text-center">
                    <Col className="text-right">
                      <button className="submit-btn usa-button usa-button-secondary" onClick={this.handleSave}>
                        Save
                      </button>
                    </Col>
                    <Col className="text-left">
                      <button className="usa-button usa-button-secondary" onClick={this.handleShowSummary} disabled={_.isEmpty(output)}>
                        Summary
                      </button>
                    </Col>
                  </Row>
                </ToggleDisplay>
              </Paper>
            </Col>
          </Row>
        </ToggleDisplay>
      </React.Fragment>
    );
  }
  private getSelectedDua() {
    if (this.props.match != null && (this.state.selectedDua === undefined || this.state.selectedDua === '')) {
      dua = this.props.match.params.selectedDua;
    }
  }

  private yearSelectionAlign() {
    const yearAlign = this.state.showState ? '' : 'year-align-left';
    const yearSelectAlign = this.state.showState ? 'ds-c-field  w-100 year-field-set' : 'ds-c-field  field-w year-field-set';
    const showYearDropdown =
      (this.state.selectedDataType !== '' && !this.state.showState) || (this.state.selectedState !== '' && this.state.showState);
    const rowMargin = { marginTop: '-18rem' };
    return { showYearDropdown, rowMargin, yearAlign, yearSelectAlign };
  }

  private toogleSelection(tooglePropertySection: boolean) {
    if (this.props.location.state !== undefined && this.props.location.state.alertOpen !== undefined) {
      tooglePropertySection = false;
      openAlert = this.props.location.state.alertOpen;
    } else if (this.props.location.state !== undefined && this.props.location.state.showPropertySection !== undefined) {
      tooglePropertySection = this.props.location.state.showPropertySection;
    }
    return tooglePropertySection;
  }
}
const mapDispatchToProps = { enableRequestButton, disableRequestButton, enableManageRequestButton, updateOutput };

const mapStateToProps = storeState => ({
  output: storeState.output.output
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(NewRequest);
