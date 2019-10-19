import { IExperiment } from 'app/shared/model/experiment.model';

export interface IDescriptor {
  id?: string;
  name?: string;
  value?: number;
  experiment?: IExperiment;
  experiment?: IExperiment;
}

export const defaultValue: Readonly<IDescriptor> = {};
