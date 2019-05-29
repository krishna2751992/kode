import './home.scss';
import React from 'react';
import { Card, CardTitle, CardText, Row, Col } from 'reactstrap';
import DuaAssignedTable from '../../shared/util/tables/dua-assigned-table';
import DuaStatusTable from '../../shared/util/tables/dua-status-table';
import Paper from '@material-ui/core/Paper';
import { enableRequestButton, enableManageRequestButton } from '../../shared/layout/header/nav-bar.reducer';
import { connect } from 'react-redux';
import http from '../../shared/service/http-service';
import _ from 'lodash';
import LoadingOverlay from 'react-loading-overlay';
import { FadeLoader } from 'react-spinners';

export class Home extends React.Component<DispatchProps> {
  state = { dropdownOpen: false, news: [], topNews: [], loading: true };
  getData = () => {
    http
      .get(`api/get-all-news`)
      .then(response => {
        this.setState({ news: response.data, loading: false });
      })
      .catch(error => {
        this.setState({ news: [], loading: false });
        console.error('error api call to api/get-all-news ' + error);
      });
  };
  componentDidMount() {
    this.getData();
    this.props.enableRequestButton();
    this.props.enableManageRequestButton();
  }

  render() {
    const { news, loading } = this.state;
    const newsAndEvents = _.filter(news, item => item.active === true);
    return (
      <Row className="mb-5">
        <Col xs="6" md="7" sm="6" xl="7" className="mb-5">
          <Paper>
            <Card body>
              <DuaStatusTable />
            </Card>
          </Paper>
          <br />
          <Paper>
            <Card body>
              <DuaAssignedTable title={'DUAs assigned to me'} fromNewRequestComponent={false} tableOptions />
            </Card>
          </Paper>
        </Col>
        <Col xs="6" md="5" sm="6" xl="5" className="pr-4">
          <Paper className="mr-2">
            <LoadingOverlay
              active={loading}
              styles={{ overlay: base => ({ ...base, background: 'rgba(0, 0, 0, 0.3)' }) }}
              spinner={<FadeLoader color={'#4A90E2'} />}
            >
              <Card body style={{ backgroundColor: '#e9ecef' }}>
                <CardTitle className="heading mt-1">News & Events</CardTitle>
                {newsAndEvents.map(data => (
                  <CardText className="news-text">
                    {data.postDate}
                    <br />
                    {data.desc}
                  </CardText>
                ))
                // <hr className="my-2" />
                }
              </Card>
            </LoadingOverlay>
          </Paper>
        </Col>
      </Row>
    );
  }
}

const mapDispatchToProps = { enableRequestButton, enableManageRequestButton };

type DispatchProps = typeof mapDispatchToProps;

export default connect(
  null,
  mapDispatchToProps
)(Home);
