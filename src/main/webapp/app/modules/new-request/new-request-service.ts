export const dataType = [{ dataType: 'INP' }, { dataType: 'HSP' }, { dataType: 'DME-POS RIC M' }];

export const dataSource = [
  { dataSource: 'NCH NEARLINE FILE' },
  { dataSource: 'SAF FILES' },
  { dataSource: 'MEDPAR - FISICAL YEAR' },
  { dataSource: 'MEDPAR - CALENDAR YEAR' },
  { dataSource: 'ENROLLMENT' },
  { dataSource: 'PART-D DATA' },
  { dataSource: 'PART-D RESTRICTED DATA' }
];

export const fields = [
  { fieldId: 0, field: 'Any Diagnosis Code' },
  { fieldId: 1, field: 'Any HCPCS Modifier Code' },
  { fieldId: 2, field: 'Beneficiary Birth Date' },
  { fieldId: 3, field: 'Beneficiary Identification Code' },
  { fieldId: 4, field: 'Beneficiary Race Code' },
  { fieldId: 5, field: 'Claim From Date' },
  { fieldId: 6, field: 'Claim Diagnosis Code' },
  { fieldId: 7, field: 'Claim Locator Number Group (HICN)' },
  { fieldId: 8, field: 'Claim Procedure Code' },
  { fieldId: 9, field: 'Claim Service Classification Type Code' },
  { fieldId: 10, field: 'Provider Number' }
];
export const availableFields = [
  'Any Diagnosis Code',
  'Any HCPCS Modifier Code',
  'Beneficiary Birth Date',
  'Beneficiary Identification Code',
  'Beneficiary Race Code',
  'Claim From Date',
  'Claim Diagnosis Code',
  'Claim Locator Number Group (HICN)',
  'Claim Procedure Code',
  'Claim Service Classification Type Code',
  'Provider Number'
];

export const years = [
  { year: '2016 06/2016' },
  { year: '2015 06/2011' },
  { year: '2014 06/2015' },
  { year: '2013 06/2014' },
  { year: '2015 06/2016' },
  { year: '2017 06/2018' },
  { year: '2018 06/2019' }
];
export const operators = [
  { operator: 'less than (<)' },
  { operator: 'greater than (>)' },
  { operator: 'equal (=)' },
  { operator: 'Range' },
  { operator: 'Not =' }
];
export const icdCode = [{ icdCode: 'ICD-10' }, { icdCode: 'ICD-9' }];

export const lookUp = [
  { field: '1 Inpatient(Incuding Part A)' },
  { field: '2 Hospital based or Inpatient (Part B only)' },
  { field: '3 Outpatient(HHA-A also)' },
  { field: '4 Other (Part B)' },
  { field: '5 International care - level I' },
  { field: '6 International care - level1 II' }
];

export const outputTypes = [
  { output: 'Whole Record Output' },
  { output: 'Whole Record View' },
  { output: 'Select Available Fields' },
  { output: 'Standard BEF-PUF View' }
];
export const states = [
  { label: 'Alabama', value: 'AL' },
  { label: 'Alaska', value: 'AK' },
  { label: 'American Samoa', value: 'AS' },
  { label: 'Arizona', value: 'AZ' },
  { label: 'Arkansas', value: 'AR' },
  { label: 'California', value: 'CA' },
  { label: 'Colorado', value: 'CO' },
  { label: 'Connecticut', value: 'CT' },
  { label: 'Delaware', value: 'DE' },
  { label: 'District Of Columbia', value: 'DC' },
  { label: 'Federated States Of Micronesia', value: 'FM' },
  { label: 'Florida', value: 'FL' },
  { label: 'Georgia', value: 'GA' },
  { label: 'Guam', value: 'GU' },
  { label: 'Hawaii', value: 'HI' },
  { label: 'Idaho', value: 'ID' },
  { label: 'Illinois', value: 'IL' },
  { label: 'Indiana', value: 'IN' },
  { label: 'Iowa', value: 'IA' },
  { label: 'Kansas', value: 'KS' },
  { label: 'Kentucky', value: 'KY' },
  { label: 'Louisiana', value: 'LA' },
  { label: 'Maine', value: 'ME' },
  { label: 'Marshall Islands', value: 'MH' },
  { label: 'Maryland', value: 'MD' },
  { label: 'Massachusetts', value: 'MA' },
  { label: 'Michigan', value: 'MI' },
  { label: 'Minnesota', value: 'MN' },
  { label: 'Mississippi', value: 'MS' },
  { label: 'Missouri', value: 'MO' },
  { label: 'Montana', value: 'MT' },
  { label: 'Nebraska', value: 'NE' },
  { label: 'Nevada', value: 'NV' },
  { label: 'New Hampshire', value: 'NH' },
  { label: 'New Jersey', value: 'NJ' },
  { label: 'New Mexico', value: 'NM' },
  { label: 'New York', value: 'NY' },
  { label: 'North Carolina', value: 'NC' },
  { label: 'North Dakota', value: 'ND' },
  { label: 'Northern Mariana Islands', value: 'MP' },
  { label: 'Ohio', value: 'OH' },
  { label: 'Oklahoma', value: 'OK' },
  { label: 'Oregon', value: 'OR' },
  { label: 'Palau', value: 'PW' },
  { label: 'Pennsylvania', value: 'PA' },
  { label: 'Puerto Rico', value: 'PR' },
  { label: 'Rhode Island', value: 'RI' },
  { label: 'South Carolina', value: 'SC' },
  { label: 'South Dakota', value: 'SD' },
  { label: 'Tennessee', value: 'TN' },
  { label: 'Texas', value: 'TX' },
  { label: 'Utah', value: 'UT' },
  { label: 'Vermont', value: 'VT' },
  { label: 'Virgin Islands', value: 'VI' },
  { label: 'Virginia', value: 'VA' },
  { label: 'Washington', value: 'WA' },
  { label: 'West Virginia', value: 'WV' },
  { label: 'Wisconsin', value: 'WI' },
  { label: 'Wyoming', value: 'WY' },
  { label: 'All States', value: 'ALL' }
];
const criteriaData = {
  criterias: {
    // 'criteria-1': { id: 'criteria-1', content: 'Claim Service Classification Type Code' },
  },
  columns: {
    'column-1': {
      id: 'column-1',
      title: 'SET 1',
      criteriaIds: [],
      rowId: []
      // criteriaIds: ['criteria-1', 'criteria-2', 'criteria-3', 'criteria-4']
    },
    'column-2': {
      id: 'column-2',
      title: 'SET 2',
      criteriaIds: [],
      rowId: []
    }
  },
  // Facilitate reordering of the columns
  columnOrder: ['column-1', 'column-2']
};

export function getCriteriaData() {
  return criteriaData;
}

export function getDataType() {
  return dataType;
}
export function getDataSource() {
  return dataSource;
}
export function getYears() {
  return years;
}
export function getStates() {
  return states;
}
export function getFields() {
  return fields;
}
export function getOperators() {
  return operators;
}
export function getIcdCode() {
  return icdCode;
}
export function getLookupValue() {
  return lookUp;
}

export function getOutputTypes() {
  return outputTypes;
}

export function getAvailableFields() {
  return availableFields;
}
