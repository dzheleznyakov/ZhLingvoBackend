import React from 'react';

import Logo from './Logo/Logo';
import AppChooser from './AppChooser/AppChooser';
import LoginButton from './LoginButton/LoginButton';
import VerticalRule from '../../../UI/VerticalRule/VerticalRule';

const Navigation = () => (
    <ul>
        <li><Logo /></li>
        <li data-nav-type="vertical-rule"><VerticalRule /></li>
        <li><AppChooser /></li>
        <li><LoginButton /></li>
    </ul>
);

export default Navigation;
