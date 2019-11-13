import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Subject from './subject';
import Project from './project';
import Experiment from './experiment';
import Descriptor from './descriptor';
import Measurement from './measurement';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/subject`} component={Subject} />
      <ErrorBoundaryRoute path={`${match.url}/project`} component={Project} />
      <ErrorBoundaryRoute path={`${match.url}/experiment`} component={Experiment} />
      <ErrorBoundaryRoute path={`${match.url}/descriptor`} component={Descriptor} />
      <ErrorBoundaryRoute path={`${match.url}/measurement`} component={Measurement} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
