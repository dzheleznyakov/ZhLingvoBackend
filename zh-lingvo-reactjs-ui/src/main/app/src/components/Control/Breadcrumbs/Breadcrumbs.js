import React from 'react';
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

import { BREADCRUMBS_TYPES } from '../../../utils/breadcrumbs';

import classes from './Breadcrumbs.module.scss';

const breadcrumbMapper = (crumbConf, lastCrumb) => {
    const postfix = lastCrumb ? '' : ' >';
    switch (crumbConf.type) {
        case BREADCRUMBS_TYPES.TEXT: 
            const { text } = crumbConf;
            return <span key={text}>{text} {postfix}</span>;
        case BREADCRUMBS_TYPES.URL:
            const { text: urlText, href, onClick = () => {} } = crumbConf;
            return (
                <span key={urlText}>
                    <Link to={href} onClick={onClick}>{urlText}</Link>{postfix}
                </span>
            );
        default: 
            return null;
    }
}

const Breadcrumbs = () => {
    const breadcrumbs = useSelector(state => state.control.breadcrumbs);
    
    const breadcrumbsElements = 
        breadcrumbs.map((conf, i) => breadcrumbMapper(conf, i === breadcrumbs.length - 1));

    return (
        <div className={classes.Breadcrumbs}>{breadcrumbsElements}</div>
    );
};

export default Breadcrumbs;
