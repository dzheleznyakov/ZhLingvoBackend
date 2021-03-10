import { useSelector } from "react-redux";

import { usernameSelector } from '../store/selectors';

export const useUsername = () => useSelector(usernameSelector);