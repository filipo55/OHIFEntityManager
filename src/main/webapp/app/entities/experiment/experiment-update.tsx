import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISubject } from 'app/shared/model/subject.model';
import { getEntities as getSubjects } from 'app/entities/subject/subject.reducer';
import { getEntity, updateEntity, createEntity, reset } from './experiment.reducer';
import { IExperiment } from 'app/shared/model/experiment.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IExperimentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IExperimentUpdateState {
  isNew: boolean;
  subjectId: string;
  subjectId: string;
}

export class ExperimentUpdate extends React.Component<IExperimentUpdateProps, IExperimentUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      subjectId: '0',
      subjectId: '0',
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

    this.props.getSubjects();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { experimentEntity } = this.props;
      const entity = {
        ...experimentEntity,
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
    this.props.history.push('/entity/experiment');
  };

  render() {
    const { experimentEntity, subjects, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="ohifEntityManagerApp.experiment.home.createOrEditLabel">Create or edit a Experiment</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : experimentEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="experiment-id">ID</Label>
                    <AvInput id="experiment-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="xnatIdLabel" for="experiment-xnatId">
                    Xnat Id
                  </Label>
                  <AvField id="experiment-xnatId" type="text" name="xnatId" />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="experiment-name">
                    Name
                  </Label>
                  <AvField id="experiment-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="dateCreatedLabel" for="experiment-dateCreated">
                    Date Created
                  </Label>
                  <AvField id="experiment-dateCreated" type="date" className="form-control" name="dateCreated" />
                </AvGroup>
                <AvGroup>
                  <Label for="experiment-subject">Subject</Label>
                  <AvInput id="experiment-subject" type="select" className="form-control" name="subject.id">
                    <option value="" key="0" />
                    {subjects
                      ? subjects.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="experiment-subject">Subject</Label>
                  <AvInput id="experiment-subject" type="select" className="form-control" name="subject.id">
                    <option value="" key="0" />
                    {subjects
                      ? subjects.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/experiment" replace color="info">
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
  subjects: storeState.subject.entities,
  experimentEntity: storeState.experiment.entity,
  loading: storeState.experiment.loading,
  updating: storeState.experiment.updating,
  updateSuccess: storeState.experiment.updateSuccess
});

const mapDispatchToProps = {
  getSubjects,
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
)(ExperimentUpdate);
