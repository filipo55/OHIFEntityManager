import { Moment } from 'moment';
import { ISubject } from 'app/shared/model/subject.model';
import { IDescriptor } from 'app/shared/model/descriptor.model';
import { IMeasurement } from 'app/shared/model/measurement.model';

export interface IExperiment {
  id?: string;
  xnatId?: string;
  name?: string;
  dateCreated?: Moment;
  subject?: ISubject;
  descriptors?: IDescriptor[];
  subject?: ISubject;
  measurements?: IMeasurement[];
}

export const defaultValue: Readonly<IExperiment> = {};
