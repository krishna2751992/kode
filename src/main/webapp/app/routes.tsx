import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import Loadable from 'react-loadable';
import Home from '../app/modules/home/home';
import ErrorBoundaryRoute from '../app/shared/error/error-boundary-route';
import PrivateRoute from '../app/shared/auth/private-route';
import NewRequest from './modules/new-request/new-request';
import NewRequestLanding from './modules/new-request/new-request-landing';
import ManageRequest from './modules/manage-request/manage-request';
import ManageNews from './modules/admin/manage-news/manage-news';
import ManageApprovals from './modules/admin/manage-approvals/manage-approvals';
import Summary from './modules/new-request/summary';
import { AUTHORITIES } from '../app/config/constants';

const Routes = () => (
  <div className="view-routes">
    <Switch>
      <ErrorBoundaryRoute path="/new-request/:selectedDua" exact component={NewRequest} />
      <ErrorBoundaryRoute path="/new-request" exact component={NewRequest} />
      <PrivateRoute path="/manage-news" exact component={ManageNews} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute path="/manage-approvals" exact component={ManageApprovals} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute path="/manage-request" exact component={ManageRequest} />
      <ErrorBoundaryRoute path="/new-request-landing" exact component={NewRequestLanding} />
      <ErrorBoundaryRoute path="/summary" exact component={Summary} />
      {/* <Route path="/not-found" component={} /> */}
      <ErrorBoundaryRoute path="/" exact component={Home} />
      {/* <Redirect to="/not-found" /> */}
    </Switch>
  </div>
);

export default Routes;
