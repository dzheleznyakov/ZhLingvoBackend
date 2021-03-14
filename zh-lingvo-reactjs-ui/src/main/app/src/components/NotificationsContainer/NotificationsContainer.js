import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import classes from './NotificationsContainer.module.scss';

const NotificationsContainer = props => {
    const errors = useSelector(state => state.app.errors);
    const [processedErrorIds, setProcessedErrorIds] = useState(new Set());

    useEffect(() => {
        const newErrors = errors
            .filter(error => !processedErrorIds.has(error.id));
        if (!newErrors.length)
            return;
        const updatedIds = new Set(processedErrorIds);
        newErrors.forEach(error => {
            console.group('New error:');
            console.log(error.descr)
            console.log(error.error.message);
            console.groupEnd();
            updatedIds.add(error.id);
        });
        setProcessedErrorIds(updatedIds);
    }, [errors, processedErrorIds, setProcessedErrorIds]);

    return <div id="notification-container" />;
};

NotificationsContainer.propTypes = {};

NotificationsContainer.defaultProps = {};

export default NotificationsContainer;
