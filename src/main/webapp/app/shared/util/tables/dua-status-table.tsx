import React from 'react';
import MUIDataTable from 'mui-datatables';
import { createMuiTheme, MuiThemeProvider } from '@material-ui/core/styles';
import http from '../../service/http-service';
import _ from 'lodash';
import LoadingOverlay from 'react-loading-overlay';
import { FadeLoader } from 'react-spinners';

class DuaStatusTable extends React.Component {
  state = {
    loading: true,
    error: '',
    requestStaus: []
  };

  componentDidMount() {
    this.getData();
  }

  getData = () => {
    http
      .get(`api/user-dua-requests`)
      .then(response => {
        this.setState({
          requestStaus: response.data,
          loading: false
        });
      })
      .catch(error => {
        this.setState({ requestStaus: [], loading: false });
        console.error('error api call to api/user-dua-requests ' + error);
      });
  };

  getMuiTheme = () =>
    createMuiTheme({
      typography: {
        useNextVariants: true
      },
      overrides: {
        MuiToolbar: {
          root: {
            marginBottom: '35px'
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
            color: '#046B99'
          }
        },
        MuiChip: {
          root: {
            color: '#046B99'
          }
        }
      }
    });

  render() {
    const columns = ['DUA Number', 'Request ID', 'Requested On', 'Status'];
    const options = {
      filter: true,
      print: false,
      download: false,
      viewColumns: false,
      search: true,
      selectableRows: false,
      responsive: 'scroll',
      rowsPerPage: 5,
      page: 1,
      rowsPerPageOptions: [5, 10, 15, 20],
      elevation: 10,
      downloadOptions: { filename: 'duaStaus.csv', separator: ',' },
      textLabels: { body: { noMatch: 'You do not have any submitted request yet!' } }
    };

    const { loading, requestStaus } = this.state;
    const userRequests = _.filter(requestStaus, item => item.requestStatus !== 'SUPER');
    const dataArray = userRequests.reduce<any[]>((all, data) => {
      all.push([data.duaNumber, data.requestId, data.createDate, data.requestStatus]);
      return all;
    }, []);

    return (
      <LoadingOverlay
        active={loading}
        styles={{ overlay: base => ({ ...base, background: 'rgba(0, 0, 0, 0.3)' }) }}
        spinner={<FadeLoader color={'#4A90E2'} />}
      >
        <MuiThemeProvider theme={this.getMuiTheme()}>
          <MUIDataTable title={<span className="heading">Request status</span>} data={dataArray} columns={columns} options={options} />
        </MuiThemeProvider>
      </LoadingOverlay>
    );
  }
}
export default DuaStatusTable;
