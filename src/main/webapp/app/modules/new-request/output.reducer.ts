export const ACTION_TYPES = {
  UPDATE_OUTPUT: 'outout/UPDATE_OUTPUT'
};

const initialState = {
  output: {} as any
};

export type OuptputState = Readonly<typeof initialState>;

// Reducer
export default (state: OuptputState = initialState, action): OuptputState => {
  switch (action.type) {
    case ACTION_TYPES.UPDATE_OUTPUT:
      return { ...state, output: action.payload };
    default:
      return state;
  }
};

// Actions

export const updateOutput = (payload: any) => dispatch => dispatch({ type: ACTION_TYPES.UPDATE_OUTPUT, payload });
