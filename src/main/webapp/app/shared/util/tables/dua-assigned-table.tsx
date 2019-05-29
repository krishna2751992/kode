import React from 'react';
import MUIDataTable from 'mui-datatables';
import { createMuiTheme, MuiThemeProvider } from '@material-ui/core/styles';
import { connect } from 'react-redux';
import Info from '@material-ui/icons/Info';
import IconButton from '@material-ui/core/IconButton';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Grid from '@material-ui/core/Grid';
import Divider from '@material-ui/core/Divider';
import Typography from '@material-ui/core/Typography';
import CloseIcon from '@material-ui/icons/Close';
import Tooltip from '@material-ui/core/Tooltip';
import AlertDialog from '../dialog/alert-dialog';
import PropTypes from 'prop-types';
import { Redirect } from 'react-router';
import http from '../../service/http-service';
import LoadingOverlay from 'react-loading-overlay';
import { FadeLoader } from 'react-spinners';

export interface IDuaAssignedTableProps extends DispatchProps {
  title: string;
  tableOptions: boolean;
  fromNewRequestComponent: boolean;
}
let dua;
const secondaryColor = '#046B99';
const tStyle = {
  fontSize: '1rem',
  fontWeight: 600,
  marginTop: '8px'
};

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

class DuaAssignedTable extends React.Component<IDuaAssignedTableProps> {
  static propTypes = {
    title: PropTypes.string.isRequired,
    tableOptions: PropTypes.bool.isRequired,
    fromNewRequestComponent: PropTypes.bool.isRequired
  };
  state = {
    rowData: [],
    userData: [],
    open: false,
    alertOpen: false,
    title: this.props.title,
    tableOptions: false,
    fromNewRequestComponent: false,
    toNewRequest: false,
    selectedDua: '',
    dataSource: [],
    encryptionSwitch: null,
    dataSourceId: null,
    loading: true,
    duaDataSource: []
  };
  componentDidMount() {
    this.getData();
  }
  getData = () => {
    http
      .get(`api/user-dua-detail`)
      .then(response => {
        this.setState({ userData: response.data, loading: false });
      })
      .catch(error => {
        this.setState({ userData: [], loading: false });
        console.error('error api call to api/user-dua-detail ' + error);
      });
  };
  getDataSource = selectedDua => {
    http
      .get(`api/get-datasource/` + selectedDua)
      .then(response => {
        this.setState({ duaDataSource: response.data });
      })
      .catch(error => {
        this.setState({ duaDataSource: [] });
        console.error('error api call to api/get-datasource/ ' + error);
      });
  };

  handleOpenDuaInfoDialog = (value, rowData) => {
    this.getDataSource(rowData.rowData[0]);
    this.setState({ open: true });
  };

  handleOpenAlertDialog = value => {
    this.setState({ selectedDua: value });
    this.setState({ alertOpen: true });
  };
  handleCloseDuaInfoDialog = () => {
    this.setState({ open: false });
  };
  handleCloseAlertDialog = () => {
    this.setState({ alertOpen: false });
  };
  // tslint:disable-next-line:ter-arrow-body-style
  handleNewRequest = value => {
    dua = value;
    this.setState({ selectedDua: value });
    this.getDataSource(value);
    this.setState({ toNewRequest: true });
  };
  render() {
    const { userData, loading } = this.state;
    const dataArray = userData.reduce<any[]>((all, data) => {
      all.push([data.duaNumber, data.expirationDate, data.requestor, data.studyName, '']);
      return all;
    }, []);

    const showTableOption = this.props.tableOptions;
    const { fromNewRequestComponent } = this.props;
    const titleToDisplay = showTableOption ? (
      <h5>{this.state.title}</h5>
    ) : (
      <Typography style={tStyle} align="left">
        {this.state.title}
      </Typography>
    );
    const fontStyle = { fontWeight: 600 };
    const columns = [
      {
        name: 'DUA Number',
        options: {
          // tslint:disable-next-line:ter-arrow-body-style
          customBodyRender: value => {
            if (showTableOption && !fromNewRequestComponent) {
              return (
                <a
                  // tslint:disable-next-line:jsx-no-lambda
                  onClick={() => {
                    this.handleOpenAlertDialog(value);
                  }}
                >
                  <u style={{ color: `${secondaryColor}` }}>{value}</u>
                </a>
              );
            }
            return (
              <a
                // tslint:disable-next-line:jsx-no-lambda
                onClick={() => {
                  this.handleNewRequest(value);
                }}
              >
                <u style={{ color: `${secondaryColor}` }}>{value}</u>
              </a>
            );
          }
        }
      },

      'Expiration Date',
      'Requester',
      'Study Name',
      {
        name: 'More Info',
        options: {
          filter: false,
          search: false,
          sort: false,
          // tslint:disable-next-line:ter-arrow-body-style
          customBodyRender: (value, rowData) => {
            return (
              <React.Fragment>
                <Tooltip title="Show More Info">
                  <IconButton aria-label="showInfo" onClick={() => this.handleOpenDuaInfoDialog(value, rowData)}>
                    <Info />
                  </IconButton>
                </Tooltip>
              </React.Fragment>
            );
          }
        }
      }
    ];
    const options = {
      filter: showTableOption,
      print: false,
      download: false,
      viewColumns: false,
      search: true,
      selectableRows: false,
      responsive: 'scroll',
      rowsPerPage: 5,
      page: 1,
      elevation: 10,
      rowsPerPageOptions: [5, 10, 15, 20],
      downloadOptions: {
        filename: 'duaAssigned.csv',
        separator: ','
      },
      textLabels: {
        body: {
          noMatch: 'Sorry we could not find any records!'
        }
      },
      onRowClick: (rowData, rowMeta) => {
        this.setState({ rowData });
      }
    };
    if (this.state.toNewRequest) {
      return (
        <Redirect
          to={{
            pathname: '/new-request/' + this.state.selectedDua,
            state: { showPropertySection: true, duaDataSource: this.state.duaDataSource }
          }}
        />
      );
    }
    return (
      <React.Fragment>
        <MuiThemeProvider theme={theme}>
          <LoadingOverlay
            active={loading}
            styles={{ overlay: base => ({ ...base, background: 'rgba(0, 0, 0, 0.3)' }) }}
            spinner={<FadeLoader color={'#4A90E2'} />}
          >
            <MUIDataTable title={titleToDisplay} data={dataArray} columns={columns} options={options} />
          </LoadingOverlay>
          <Dialog maxWidth="sm" open={this.state.open} onClose={this.handleCloseDuaInfoDialog} aria-labelledby="dialog-title">
            <DialogTitle id="dialog-title" className="p-0">
              <Grid container alignItems="center">
                <Grid item xs />
                <Grid item>
                  <IconButton aria-label="Close" onClick={this.handleCloseDuaInfoDialog}>
                    <CloseIcon />
                  </IconButton>
                </Grid>
              </Grid>
            </DialogTitle>
            <DialogContent>
              <DialogContentText>
                <Grid container alignItems="center">
                  <Grid container spacing={40} className="mb-1">
                    <Grid item xs={6}>
                      Dua Number
                      <Typography component={'span'} style={fontStyle}>
                        {(dua = this.state.rowData[0])}
                      </Typography>
                    </Grid>
                    <Grid item xs={6}>
                      Expiration Date
                      <Typography component={'span'} style={fontStyle}>
                        {this.state.rowData[1]}
                      </Typography>
                    </Grid>
                  </Grid>
                  <Grid container spacing={40} className="mb-0">
                    <Grid item xs={6}>
                      Requester
                      <Typography component={'span'} style={fontStyle}>
                        {this.state.rowData[2]}
                      </Typography>
                    </Grid>
                    <Grid item xs={6}>
                      Study Name
                      <Typography component={'span'} style={fontStyle}>
                        {this.state.rowData[3]}
                      </Typography>
                    </Grid>
                  </Grid>
                  <Grid container className="mb-4">
                    <Grid item xs={12}>
                      <Divider variant="fullWidth" />
                    </Grid>
                  </Grid>
                  <Grid container spacing={40}>
                    <Grid item xs={6}>
                      Available Data Files:
                      <ul>
                        {this.state.duaDataSource.map(d => (
                          <li key={d}>
                            <Typography component={'span'} style={fontStyle}>
                              {d.name}
                            </Typography>
                          </li>
                        ))}
                      </ul>
                    </Grid>
                    {/* <Grid item xs={6}>
                      Available Years Of Data:
                      <ul>
                        {years.map(y => (
                          <li key={y}>
                            <Typography style={fontStyle}>{y}</Typography>
                          </li>
                        ))}
                      </ul>
                    </Grid>*/}
                  </Grid>
                </Grid>
              </DialogContentText>
            </DialogContent>
            <DialogActions />
          </Dialog>
          <AlertDialog
            alertOpen={this.state.alertOpen}
            onClose={this.handleCloseAlertDialog}
            fromHome
            selectedDua={this.state.selectedDua}
            dataSource={this.state.userData}
          />
        </MuiThemeProvider>
      </React.Fragment>
    );
  }
}

const mapDispatchToProps = {};
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  null,
  mapDispatchToProps
)(DuaAssignedTable);
