import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './experiment.reducer';
import { IExperiment } from 'app/shared/model/experiment.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IExperimentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ExperimentDetail extends React.Component<IExperimentDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { experimentEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Experiment [<b>{experimentEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="xnatId">Xnat Id</span>
            </dt>
            <dd>{experimentEntity.xnatId}</dd>
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{experimentEntity.name}</dd>
            <dt>
              <span id="dateCreated">Date Created</span>
            </dt>
            <dd>
              <TextFormat value={experimentEntity.dateCreated} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>Subject</dt>
            <dd>{experimentEntity.subject ? experimentEntity.subject.id : ''}</dd>
            <dt>Subject</dt>
            <dd>{experimentEntity.subject ? experimentEntity.subject.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/experiment" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/experiment/${experimentEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ experiment }: IRootState) => ({
  experimentEntity: experiment.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ExperimentDetail);
