import React from 'react';
import PropTypes from 'prop-types';

import classes from './Layout.module.scss';

import Toolbar from '../../components/Toolbar/Toolbar';

const Layout = props => {
    const { children } = props;

    return (
        <div className={classes.Layout}>
            <div className={classes.Toolbar}><Toolbar /></div>
            <div className={classes.Breadcrumb}>Breadcrumb</div>
            <div className={classes.Content}>
                {children}
            </div>
            <div className={classes.Footer}>Footer</div>
        </div>
    );
};

Layout.propTypes = {};

Layout.defaultProps = {};

export default Layout;
