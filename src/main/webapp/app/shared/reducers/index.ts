import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from '../../../app/modules/administration/administration.reducer';

import navbar, { NavBarState } from '../layout/header/nav-bar.reducer';
import output, { OuptputState } from '../../modules/new-request/output.reducer';
import authentication, { AuthenticationState } from './authentication';

/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;

  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
  readonly navbar: NavBarState;
  readonly output: OuptputState;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,

  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
  navbar,
  output
});

export default rootReducer;
