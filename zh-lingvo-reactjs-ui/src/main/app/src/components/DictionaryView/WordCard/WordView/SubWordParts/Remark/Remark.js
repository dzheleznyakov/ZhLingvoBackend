import React from 'react';
import PropTypes from 'prop-types';

import classes from './Remark.module.scss';

const Remark = props => {
    const { value = null } = props;
    return value && <span className={classes.Remark}>{value}</span>;
};

Remark.propTypes = {
    value: PropTypes.string,
};

export default Remark;
