import { IExperiment } from 'app/shared/model/experiment.model';
import { IDescriptor } from 'app/shared/model/descriptor.model';

export interface IMeasurement {
  id?: string;
  name?: string;
  descriptors?: IDescriptor[];
  experiment?: IExperiment;
}

export const defaultValue: Readonly<IMeasurement> = {};
