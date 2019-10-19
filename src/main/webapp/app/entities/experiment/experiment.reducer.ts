import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IExperiment, defaultValue } from 'app/shared/model/experiment.model';

export const ACTION_TYPES = {
  FETCH_EXPERIMENT_LIST: 'experiment/FETCH_EXPERIMENT_LIST',
  FETCH_EXPERIMENT: 'experiment/FETCH_EXPERIMENT',
  CREATE_EXPERIMENT: 'experiment/CREATE_EXPERIMENT',
  UPDATE_EXPERIMENT: 'experiment/UPDATE_EXPERIMENT',
  DELETE_EXPERIMENT: 'experiment/DELETE_EXPERIMENT',
  RESET: 'experiment/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IExperiment>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ExperimentState = Readonly<typeof initialState>;

// Reducer

export default (state: ExperimentState = initialState, action): ExperimentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EXPERIMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EXPERIMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EXPERIMENT):
    case REQUEST(ACTION_TYPES.UPDATE_EXPERIMENT):
    case REQUEST(ACTION_TYPES.DELETE_EXPERIMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EXPERIMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EXPERIMENT):
    case FAILURE(ACTION_TYPES.CREATE_EXPERIMENT):
    case FAILURE(ACTION_TYPES.UPDATE_EXPERIMENT):
    case FAILURE(ACTION_TYPES.DELETE_EXPERIMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EXPERIMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_EXPERIMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EXPERIMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_EXPERIMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EXPERIMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/experiments';

// Actions

export const getEntities: ICrudGetAllAction<IExperiment> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_EXPERIMENT_LIST,
    payload: axios.get<IExperiment>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IExperiment> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EXPERIMENT,
    payload: axios.get<IExperiment>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IExperiment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EXPERIMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IExperiment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EXPERIMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IExperiment> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EXPERIMENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
