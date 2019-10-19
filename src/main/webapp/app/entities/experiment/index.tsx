import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Experiment from './experiment';
import ExperimentDetail from './experiment-detail';
import ExperimentUpdate from './experiment-update';
import ExperimentDeleteDialog from './experiment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExperimentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExperimentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExperimentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Experiment} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ExperimentDeleteDialog} />
  </>
);

export default Routes;
