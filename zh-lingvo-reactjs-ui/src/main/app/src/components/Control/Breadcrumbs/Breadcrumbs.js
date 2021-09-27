import React from 'react';
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

import { BREADCRUMBS_TYPES } from '../../../utils/breadcrumbs';

import classes from './Breadcrumbs.module.scss';

const breadcrumbMapper = crumbConf => {
    switch (crumbConf.type) {
        case BREADCRUMBS_TYPES.TEXT: 
            const { text } = crumbConf;
            return <span key={text}>{text} {'>'}</span>;
        case BREADCRUMBS_TYPES.URL:
            const { text: urlText, href, onClick = () => {} } = crumbConf;
            return (
                <span key={urlText}>
                    <Link to={href} onClick={onClick}>{urlText}</Link>{' >'}
                </span>
            );
        default: 
            return null;
    }
}

const Breadcrumbs = () => {
    const breadcrumbs = useSelector(state => state.control.breadcrumbs);
    
    const breadcrumbsElement = breadcrumbs.map(breadcrumbMapper);

    return (
        <div className={classes.Breadcrumbs}>{breadcrumbsElement}</div>
    );
};

export default Breadcrumbs;
