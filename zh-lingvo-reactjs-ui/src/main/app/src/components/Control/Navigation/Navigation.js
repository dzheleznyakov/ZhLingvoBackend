import React from 'react';
import PropTypes from 'prop-types';

import Logo from './Logo/Logo';
import AppChooser from './AppChooser/AppChooser';
import LoginButton from './LoginButton/LoginButton';
import VerticalRule from '../../UI/VerticalRule/VerticalRule';

const Navigation = props => {
    const { onLogoClicked, onLoginClicked } = props;
    return (
        <ul>
            <li><Logo postClicked={onLogoClicked} /></li>
            <li data-nav-type="vertical-rule"><VerticalRule /></li>
            <li><AppChooser /></li>
            <li><LoginButton postClicked={onLoginClicked} /></li>
        </ul>
    )
};

Navigation.propTypes = {
    onLogoClicked: PropTypes.func,
    onLoginClicked: PropTypes.func,
};

export default Navigation;
