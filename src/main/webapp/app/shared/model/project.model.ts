import { ISubject } from 'app/shared/model/subject.model';

export interface IProject {
  id?: string;
  xnatId?: string;
  name?: string;
  subjects?: ISubject[];
}

export const defaultValue: Readonly<IProject> = {};
