import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IExperiment } from 'app/shared/model/experiment.model';
import { getEntities as getExperiments } from 'app/entities/experiment/experiment.reducer';
import { getEntity, updateEntity, createEntity, reset } from './measurement.reducer';
import { IMeasurement } from 'app/shared/model/measurement.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMeasurementUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMeasurementUpdateState {
  isNew: boolean;
  measurementId: string;
  experimentId: string;
}

export class MeasurementUpdate extends React.Component<IMeasurementUpdateProps, IMeasurementUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      measurementId: '0',
      experimentId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getExperiments();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { measurementEntity } = this.props;
      const entity = {
        ...measurementEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/measurement');
  };

  render() {
    const { measurementEntity, experiments, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="ohifEntityManagerApp.measurement.home.createOrEditLabel">Create or edit a Measurement</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : measurementEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="measurement-id">ID</Label>
                    <AvInput id="measurement-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="measurement-name">
                    Name
                  </Label>
                  <AvField id="measurement-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label for="measurement-measurement">Measurement</Label>
                  <AvInput id="measurement-measurement" type="select" className="form-control" name="measurement.id">
                    <option value="" key="0" />
                    {experiments
                      ? experiments.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="measurement-experiment">Experiment</Label>
                  <AvInput id="measurement-experiment" type="select" className="form-control" name="experiment.id">
                    <option value="" key="0" />
                    {experiments
                      ? experiments.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/measurement" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  experiments: storeState.experiment.entities,
  measurementEntity: storeState.measurement.entity,
  loading: storeState.measurement.loading,
  updating: storeState.measurement.updating,
  updateSuccess: storeState.measurement.updateSuccess
});

const mapDispatchToProps = {
  getExperiments,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MeasurementUpdate);
