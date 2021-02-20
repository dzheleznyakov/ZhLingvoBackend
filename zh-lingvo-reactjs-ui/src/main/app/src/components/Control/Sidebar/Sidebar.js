import React, { useState } from 'react';

import classes from './Sidebar.module.scss';

import Navigation from '../Navigation/Navigation';
import HamburgerButton from '../../UI/HamburgerButton/HamburgerButton';
import Backdrop from '../../UI/Backdrop/Backdrop';

const Sidebar = () => {
    const [showSidebar, setShowSidebar] = useState(false);

    const navigationClass = showSidebar ? classes.shown : classes.hidden;
    const buttonClass = showSidebar ? classes.hidden : classes.shown;

    const onButtonClicked = () => setShowSidebar(!showSidebar);
    const onBackdropClicked = () => setShowSidebar(false);

    return (
        <div className={classes.Sidebar}>
            <Backdrop 
                show={showSidebar}
                clicked={onBackdropClicked}
                zIndex={25}
            />    
            <div className={[classes.HamburgerButton, buttonClass].join(' ')}>
                <HamburgerButton clicked={onButtonClicked} />
            </div>
            <div className={navigationClass}>
                <Navigation />
            </div>
        </div>
    )
};

export default Sidebar;
