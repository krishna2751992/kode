import './dialog.scss';
import React from 'react';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Grid from '@material-ui/core/Grid';
import { Redirect } from 'react-router-dom';

export interface IAlertDialogProps {
  alertOpen: boolean;
  onClose: any;
  fromHome: boolean;
  selectedDua: string;
  dataSource: any;
}

class AlertDialog extends React.Component<IAlertDialogProps> {
  state = {
    alertOpen: false,
    toNewRequestLanding: false,
    selectedDua: ''
  };

  handleClose = () => {
    this.setState({ toNewRequestLanding: true });
    this.props.onClose();
  };

  render() {
    const { onClose, alertOpen, fromHome, selectedDua, dataSource } = this.props;
    const { toNewRequestLanding } = this.state;

    if (toNewRequestLanding) {
      return (
        <Redirect
          to={{
            pathname: '/new-request/' + selectedDua,
            state: {
              showPropertySection: fromHome === undefined ? true : fromHome,
              dataSource
            }
          }}
        />
      );
    }
    return (
      <React.Fragment>
        <Dialog maxWidth="md" open={alertOpen} onClose={onClose} aria-labelledby="dialog-title">
          <DialogTitle id="dialog-title" className="mb-3 mt-2">
            <Grid container>
              <Grid item xs={10}>
                <h1 className="ds-h2" id="dialog-title">
                  Request Processing Times
                </h1>
              </Grid>
              <Grid id="dialogClose" item xs={2} className="pl-5">
                <button
                  className="ds-c-button ds-c-button--transparent ds-c-dialog__close mt-0"
                  aria-label="Close modal dialog"
                  onClick={this.handleClose}
                >
                  Close
                </button>
              </Grid>
            </Grid>
          </DialogTitle>
          <DialogContent>
            <DialogContentText className="mt-0">
              <div className="body-text">DESY processing times are impacted by inherited system and application constraints.</div>
              <div className="body-text">Depending on the number of competing requests, the average estimated turn-around</div>
              <div className="body-text">times are as below:</div>

              <table className="ds-c-table mt-3 mb-5">
                <thead>
                  <tr>
                    <th scope="col">TYPE OF DATA</th>
                    <th scope="col">APPROXIMATE PROCESSING TIME</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>NCH Part A</td>
                    <td>3-6 weeks</td>
                  </tr>
                  <tr>
                    <td>NCH Part B</td>
                    <td>4-8 weeks</td>
                  </tr>
                  <tr>
                    <td>Part A (excluding Outpatient) SAF</td>
                    <td>1-3 weeks</td>
                  </tr>
                  <tr>
                    <td>Part B and Outpatient SAF</td>
                    <td>2-4 weeks</td>
                  </tr>
                </tbody>
              </table>

              <div className="body-text">Please be sure to plan accordingly when submitting your requests.</div>
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <button onClick={this.handleClose} style={{ margin: 'auto' }} className="ds-c-button ds-c-button--primary">
              Ok
            </button>
          </DialogActions>
        </Dialog>
      </React.Fragment>
    );
  }
}
export default AlertDialog;
