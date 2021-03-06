import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './descriptor.reducer';
import { IDescriptor } from 'app/shared/model/descriptor.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDescriptorDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DescriptorDetail extends React.Component<IDescriptorDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { descriptorEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Descriptor [<b>{descriptorEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{descriptorEntity.name}</dd>
            <dt>
              <span id="value">Value</span>
            </dt>
            <dd>{descriptorEntity.value}</dd>
            <dt>Measurement</dt>
            <dd>{descriptorEntity.measurement ? descriptorEntity.measurement.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/descriptor" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/descriptor/${descriptorEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ descriptor }: IRootState) => ({
  descriptorEntity: descriptor.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DescriptorDetail);
