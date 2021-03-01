import React from 'react';
import { useSelector } from 'react-redux';

import classes from './Layout.module.scss';

import Toolbar from '../../components/Control/Toolbar/Toolbar';
import Sidebar from '../../components/Control/Sidebar/Sidebar';
import Breadcrumbs from '../../components/Control/Breadcrumbs/Breadcrumbs';

const Layout = props => {
    const { children } = props;
    const showSidebar = useSelector(store => store.app.showSidebar);

    const sidebarClasses = [classes.Sidebar];
    if (!showSidebar)
        sidebarClasses.push(classes['Sidebar--hidden']);

    return (
        <div className={classes.Layout}>
            <div className={classes.Toolbar}><Toolbar /></div>
            <div className={classes.Breadcrumb}><Breadcrumbs /></div>
            <div className={sidebarClasses.join(' ')}><Sidebar /></div>
            <div className={classes.Footer}>Footer</div>
            <div className={classes.Content}>{children}</div>
        </div>
    );
};

export default Layout;
