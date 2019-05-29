import React from 'react';
import styled from 'styled-components';
import { Draggable } from 'react-beautiful-dnd';

const Container = styled.div`
  padding: 8px;
  margin-bottom: 8px;
  transition: background-color 0.2s ease;
  border-radius: 20px;
  box-shadow: 0 2px 4px 0 #dadee3;
  font-family: Source Sans Pro;
  font-size: 1.1rem;
  font-weight: bold;
  font-style: normal;
  font-stretch: normal;
  line-height: 1.7;
  letter-spacing: normal;
  color: black;
`;

const Criteria = props => {
  const isDragDisabled = props.criteriaList.length === 1 && props.columnTitle === 'SET 1';
  return (
    <React.Fragment>
      <Draggable draggableId={props.criteria.id} index={props.index} isDragDisabled={isDragDisabled}>
        {(provided, snapshot) => (
          <Container
            className={isDragDisabled ? 'criteria-column-disabled' : 'criteria-column-selection'}
            {...provided.draggableProps}
            {...provided.dragHandleProps}
            ref={provided.innerRef}
            isDragging={snapshot.isDragging}
          >
            {props.criteria.content}
          </Container>
        )}
      </Draggable>
    </React.Fragment>
  );
};
export default Criteria;
