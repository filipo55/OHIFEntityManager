import { IExperiment } from 'app/shared/model/experiment.model';
import { IProject } from 'app/shared/model/project.model';

export interface ISubject {
  id?: string;
  xnatId?: string;
  name?: string;
  experiments?: IExperiment[];
  project?: IProject;
  project?: IProject;
}

export const defaultValue: Readonly<ISubject> = {};
