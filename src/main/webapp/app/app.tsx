import './app.scss';
import 'react-toastify/dist/ReactToastify.css';
// tslint:disable-next-line:no-submodule-imports
import '@cmsgov/design-system-core/dist/index.css';
import './uswds.css';
import React from 'react';
import { connect } from 'react-redux';
import { HashRouter as Router } from 'react-router-dom';
import { ToastContainer, ToastPosition, toast } from 'react-toastify';
import { IRootState } from '../app/shared/reducers';
import ErrorBoundary from '../app/shared/error/error-boundary';
import { getSession } from '../app/shared/reducers/authentication';
import AppRoutes from '../app/routes';
import NavAppBar from '../app/shared/layout/header/nav-bar';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import { AUTHORITIES } from '../app/config/constants';
import { hasAnyAuthority } from '../app/shared/auth/private-route';

console.log(' environment   ' + process.env.NODE_ENV);

const theme = createMuiTheme({
  overrides: {
    MuiButton: {
      // Name of the component  / style sheet
      text: {
        // Name of the rule
        color: 'white' // Some CSS
      }
    }
  },
  palette: {
    primary: {
      main: '#0071BC'
    },
    secondary: {
      main: '#00A6D2'
    },
    text: {
      primary: '#000',
      secondary: '#fff'
    }
  },
  typography: {
    htmlFontSize: 18,
    fontFamily: ['Source Sans Pro', 'Merriweather'].join(','),
    useNextVariants: true
  }
});

export interface IAppProps extends StateProps, DispatchProps {}

export class App extends React.Component<IAppProps> {
  componentDidMount() {
    this.props.getSession();
  }
  render() {
    return (
      <MuiThemeProvider theme={theme}>
        <Router>
          <div className="app-container">
            <ToastContainer
              position={toast.POSITION.TOP_RIGHT as ToastPosition}
              className="toastify-container"
              toastClassName="toastify-toast"
            />
            <ErrorBoundary>
              <NavAppBar isAdmin={this.props.isAdmin} />
            </ErrorBoundary>
            <div className="view-container">
              <ErrorBoundary>
                <AppRoutes />
              </ErrorBoundary>
            </div>
          </div>
        </Router>
      </MuiThemeProvider>
    );
  }
}

const mapStateToProps = ({ authentication, applicationProfile }: IRootState) => ({
  isAdmin: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN]),
  ribbonEnv: applicationProfile.ribbonEnv,
  isInProduction: applicationProfile.inProduction,
  isSwaggerEnabled: applicationProfile.isSwaggerEnabled
});

const mapDispatchToProps = { getSession };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(App);
