import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from './user-management';
// prettier-ignore
import subject, {
  SubjectState
} from 'app/entities/subject/subject.reducer';
// prettier-ignore
import project, {
  ProjectState
} from 'app/entities/project/project.reducer';
// prettier-ignore
import experiment, {
  ExperimentState
} from 'app/entities/experiment/experiment.reducer';
// prettier-ignore
import descriptor, {
  DescriptorState
} from 'app/entities/descriptor/descriptor.reducer';
// prettier-ignore
import measurement, {
  MeasurementState
} from 'app/entities/measurement/measurement.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly subject: SubjectState;
  readonly project: ProjectState;
  readonly experiment: ExperimentState;
  readonly descriptor: DescriptorState;
  readonly measurement: MeasurementState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  subject,
  project,
  experiment,
  descriptor,
  measurement,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
