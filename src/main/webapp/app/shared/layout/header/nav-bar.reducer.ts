export const ACTION_TYPES = {
  ENABLE_BUTTON: 'navbar/ENABLE_BUTTON',
  DISABLE_BUTTON: 'navbar/DISABLE_BUTTON',
  ENABLE_MANAGE_BUTTON: 'navbar/ENABLE_MANAGE_BUTTON',
  DISABLE_MANAGE_BUTTON: 'navbar/DISABLE_MANAGE_BUTTON'
};
const initialState = {
  isRequestButtonEnabled: true,
  isManageRequestButtonEnabled: true
};

export type NavBarState = Readonly<typeof initialState>;

// Reducer
export default (state: NavBarState = initialState, action): NavBarState => {
  switch (action.type) {
    case ACTION_TYPES.ENABLE_BUTTON:
      return {
        ...state,
        isRequestButtonEnabled: action.payload
      };
    case ACTION_TYPES.DISABLE_BUTTON:
      return {
        ...state,
        isRequestButtonEnabled: action.payload
      };
    case ACTION_TYPES.ENABLE_MANAGE_BUTTON:
      return {
        ...state,
        isManageRequestButtonEnabled: action.payload
      };
    case ACTION_TYPES.DISABLE_MANAGE_BUTTON:
      return {
        ...state,
        isManageRequestButtonEnabled: action.payload
      };
    default:
      return state;
  }
};

// Actions
export const enableRequestButton = () => dispatch =>
  dispatch({
    type: ACTION_TYPES.ENABLE_BUTTON,
    payload: true
  });

export const disableRequestButton = () => dispatch =>
  dispatch({
    type: ACTION_TYPES.DISABLE_BUTTON,
    payload: false
  });

// manage request button actions
export const enableManageRequestButton = () => dispatch =>
  dispatch({
    type: ACTION_TYPES.ENABLE_MANAGE_BUTTON,
    payload: true
  });

export const disableManageRequestButton = () => dispatch =>
  dispatch({
    type: ACTION_TYPES.DISABLE_MANAGE_BUTTON,
    payload: false
  });
