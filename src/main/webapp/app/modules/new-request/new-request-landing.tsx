import React from 'react';
import * as H from 'history';
import NewRequest from './new-request';
import { RouteComponentProps } from 'react-router-dom';

interface INewRequestLandingProps {
  location: H.Location;
  showPropertySection: boolean;
}
interface INewRequestLandingState {
  showPropertySection: boolean;
}

class NewRequestLanding extends React.Component<INewRequestLandingProps & RouteComponentProps, INewRequestLandingState> {
  state: INewRequestLandingState = {
    showPropertySection: true
  };
  render() {
    return (
      <NewRequest
        showPropertySection
        dataSource
        history={this.props.history}
        location={this.props.location}
        match={null}
        staticContext={this.props.staticContext}
      />
    );
  }
}

export default NewRequestLanding;
