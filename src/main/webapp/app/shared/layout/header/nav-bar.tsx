import './header.scss';
import React from 'react';
import {
  MDBNavbar,
  MDBNavbarBrand,
  MDBNavbarNav,
  MDBNavItem,
  MDBNavLink,
  MDBNavbarToggler,
  MDBCollapse,
  MDBDropdown,
  MDBDropdownToggle,
  MDBDropdownMenu
} from 'mdbreact';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { disableRequestButton, enableRequestButton, enableManageRequestButton, disableManageRequestButton } from './nav-bar.reducer';
// tslint:disable-next-line:no-submodule-imports
import Button from '@cmsgov/design-system-core/dist/components/Button/Button';
import { RouteComponentProps, withRouter } from 'react-router';

export interface INavBarProps extends StateProps, DispatchProps, RouteComponentProps<{}> {
  isAdmin: boolean;
}

class NavBar extends React.Component<INavBarProps> {
  state = {
    isOpen: false
  };

  toggleCollapse = () => {
    this.setState({ isOpen: !this.state.isOpen });
  };
  handleOnClick = () => {
    this.props.history.push({
      pathname: '/new-request-landing',
      state: {
        fromHome: false,
        showPropertiesSection: false,
        alertOpen: true
      }
    });
  };
  render() {
    const { isRequestButtonEnabled, isManageRequestButtonEnabled, isAdmin } = this.props;

    return (
      <header className="usa-banner-header">
        <MDBNavbar color="indigo" dark expand="md">
          <MDBNavbarBrand>
            <Link to="/" id="desyHomeLink">
              <span className="app-title" data-toggle="home" data-placement="bottom" title="home">
                Data Extract System (DESY)
              </span>
            </Link>
          </MDBNavbarBrand>
          <MDBNavbarToggler onClick={this.toggleCollapse} />
          <MDBCollapse id="navbarCollapse3" isOpen={this.state.isOpen} navbar>
            <MDBNavbarNav right>
              <MDBNavItem>
                {isAdmin && (
                  <MDBDropdown>
                    <MDBDropdownToggle nav caret>
                      <span className=" menu-text mr-2">Admin</span>
                    </MDBDropdownToggle>
                    <MDBDropdownMenu id="dropdown" className="dropdown-for-admin">
                      <MDBNavLink to="/manage-approvals">Manage Approvals</MDBNavLink>
                      <MDBNavLink to="/manage-news">Manage News</MDBNavLink>
                    </MDBDropdownMenu>
                  </MDBDropdown>
                )}
              </MDBNavItem>
              <MDBNavItem className="mr-3">
                <MDBNavLink to="/manage-request" className={isManageRequestButtonEnabled ? 'menu-text' : 'visited-color'}>
                  Manage Requests
                </MDBNavLink>
              </MDBNavItem>
            </MDBNavbarNav>
            <Button
              className={isRequestButtonEnabled ? 'usa-button usa-button-secondary-inverse mb-0' : 'usa-button visited-color mb-0'}
              onClick={this.handleOnClick}
              disabled={!isRequestButtonEnabled}
              htmlFor="new-request"
            >
              New Request
            </Button>
          </MDBCollapse>
        </MDBNavbar>
      </header>
    );
  }
}

const mapStateToProps = storeState => ({
  isRequestButtonEnabled: storeState.navbar.isRequestButtonEnabled,
  isManageRequestButtonEnabled: storeState.navbar.isManageRequestButtonEnabled
});

const mapDispatchToProps = { disableRequestButton, enableRequestButton, enableManageRequestButton, disableManageRequestButton };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default withRouter(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(NavBar)
);
