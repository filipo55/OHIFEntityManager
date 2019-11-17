import { IMeasurement } from 'app/shared/model/measurement.model';

export interface IDescriptor {
  id?: string;
  name?: string;
  value?: number;
  measurement?: IMeasurement;
}

export const defaultValue: Readonly<IDescriptor> = {};
