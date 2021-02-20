import React from 'react';
import PropTypes from 'prop-types';

import classes from './Layout.module.scss';

import Toolbar from '../../components/Control/Toolbar/Toolbar';
import Sidebar from '../../components/Control/Sidebar/Sidebar';

const Layout = props => {
    const { children } = props;

    return (
        <div className={classes.Layout}>
            <div className={classes.Toolbar}><Toolbar /></div>
            <div className={classes.Breadcrumb}>Breadcrumb</div>
            <div className={classes.Content}>{children}</div>
            <div className={classes.Sidebar}><Sidebar /></div>
            <div className={classes.Footer}>Footer</div>
        </div>
    );
};

Layout.propTypes = {};

Layout.defaultProps = {};

export default Layout;
