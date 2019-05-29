import React from 'react';
import '../../../../app/app.scss';
import { withStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  menuButton: {
    marginLeft: -18,
    marginRight: 10
  }
});

function FooterAppBar(props) {
  const { classes } = props;
  return (
    <div className={classes.root}>
      <AppBar position="static">
        <Toolbar>
          <Typography color="textSecondary">CMS</Typography>
        </Toolbar>
      </AppBar>
    </div>
  );
}

export default withStyles(styles)(FooterAppBar);
