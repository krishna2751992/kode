import React from 'react';
import styled from 'styled-components';
import { Droppable } from 'react-beautiful-dnd';
import Criteria from './criteria';

const Container = styled.div`
  margin: 8px;
  border: 1px solid lightgrey;
  border-radius: 2px;
  width: 220px;
  display: flex;
  flex-direction: column;
`;
const Title = styled.h3`
  padding: 8px;
  margin: auto;
  font-size: 1.2rem;
  font-weight: bold;
  color: black;
`;
const CriteriaList = styled.div`
  padding: 8px;
  transition: background-color 0.2s ease;
  background-color: ${props => (props.isDraggingOver ? '#dce4ef' : 'white')}
  flex-grow: 1;
  min-height: 100px;
`;
// tslint:disable-next-line:ter-arrow-body-style
const Column = props => {
  const criteriaList = props.criterias;
  const columnTitle = props.column.title;
  return (
    <Container>
      <Title>{props.column.title}</Title>
      <Droppable droppableId={props.column.id} type="CRITERIA">
        {(provided, snapshot) => (
          <CriteriaList ref={provided.innerRef} {...provided.droppableProps} isDraggingOver={snapshot.isDraggingOver}>
            {props.criterias.map((criteria, index) => (
              <Criteria key={criteria.id} criteria={criteria} index={index} criteriaList={criteriaList} columnTitle={columnTitle} />
            ))}
            {provided.placeholder}
          </CriteriaList>
        )}
      </Droppable>
    </Container>
  );
};
export default Column;
