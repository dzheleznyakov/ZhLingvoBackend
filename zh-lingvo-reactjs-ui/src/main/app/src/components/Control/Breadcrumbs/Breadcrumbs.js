import React from 'react';
import { useSelector } from 'react-redux';

import classes from './Breadcrumbs.module.scss';

const Breadcrumbs = () => {
    const breadcrumbs = useSelector(state => state.control.breadcrumbs);

    return (
        <div className={classes.Breadcrumbs}>{breadcrumbs.join(' > ')} {'>'} </div>
    );
};

export default Breadcrumbs;
