import React from 'react';
import { useSelector, useDispatch } from 'react-redux';

import classes from './Sidebar.module.scss';

import Navigation from '../Navigation/Navigation';
import HamburgerButton from '../../UI/HamburgerButton/HamburgerButton';
import Backdrop from '../../UI/Backdrop/Backdrop';
import * as actions from '../../../store/actions';

const Sidebar = () => {
    const showSidebar = useSelector(state => state.app.showSidebar);
    const dispatch = useDispatch();
    const setShowSidebar = value => dispatch(actions.showSidebar(value));

    const navigationClass = showSidebar ? classes.shown : classes.hidden;
    const buttonClass = showSidebar ? classes.hidden : classes.shown;

    const onButtonClicked = () => setShowSidebar(!showSidebar);
    const hideSidebar = () => setShowSidebar(false);

    return (
        <div className={classes.Sidebar}>
            <Backdrop 
                show={showSidebar}
                clicked={hideSidebar}
            />    
            <div className={[classes.HamburgerButton, buttonClass].join(' ')}>
                <HamburgerButton clicked={onButtonClicked} />
            </div>
            <div className={navigationClass}>
                <Navigation
                    onLogoClicked={hideSidebar}
                    onLoginClicked={hideSidebar} 
                />
            </div>
        </div>
    )
};

export default Sidebar;
