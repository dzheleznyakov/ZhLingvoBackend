import React, { useState } from 'react';
import PropTypes from 'prop-types';

import classes from './CollapsableListView.module.scss';

const CollabsableListView = props => {
    const { heading, items, collaplsedDefault } = props;
    const [collapsed, setCollapsed] = useState(collaplsedDefault);

    const headingClasses = [
        classes.Heading,
        collapsed ? null : classes.Expanded,
    ];
    const toggleCollapsed = () => setCollapsed(col => !col);

    return (
        <ul>
            <div className={headingClasses.join(' ')} onClick={toggleCollapsed}>{heading}</div>
            {!collapsed && items.map(({ key,  node }) => <li className={classes.ListItem} key={key}>{node}</li>)}
        </ul>
    );
};

CollabsableListView.propTypes = {
    heading: PropTypes.string.isRequired,
    items: PropTypes.arrayOf(PropTypes.shape({
        key: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
        node: PropTypes.node.isRequired,
    })),
    collaplsedDefault: PropTypes.bool,
};

CollabsableListView.defaultProps = {
    items: [],
    collaplsedDefault: true,
};

export default CollabsableListView;
