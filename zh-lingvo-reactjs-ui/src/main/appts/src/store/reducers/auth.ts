import { createAsyncThunk, createSlice, isRejected } from '@reduxjs/toolkit';

import api from '../../features/http/axios-api';
import { deleteCookie, setCookie } from '../../utils/cookies';

type AuthState = {
  username: string | null;
  error: string | null;
  status: 'idle' | 'loading' | 'error';
};

const initialState: AuthState = {
  username: null,
  error: null,
  status: 'idle',
};

const AUTH_TOKEN_COOKIE_FIELD = 'authToken';

const signIn = createAsyncThunk('auth/signIn', async (username: string) => {
  const response = await api.login('/signin', { username });
  const { token } = response.data;
  setCookie(AUTH_TOKEN_COOKIE_FIELD, token);
  return { username };
});

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    signOut(state) {
      state.username = null;
      deleteCookie(AUTH_TOKEN_COOKIE_FIELD);
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(signIn.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(signIn.fulfilled, (state, action) => {
        state.status = 'idle';
        const { username } = action.payload;
        state.username = username;
      })
      .addMatcher(isRejected, (state, action) => {
        state.status = 'error';
        state.error = action?.error?.message ?? null;
      });
  },
});

export const authReducer = authSlice.reducer;
export const authActions = { ...authSlice.actions, signIn };
