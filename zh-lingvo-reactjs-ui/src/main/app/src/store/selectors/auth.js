// import { createSelector } from 'reselect';

export const usernameSelector = state => state.auth.username;

export const loggedInSelector = state => state.auth.username != null;