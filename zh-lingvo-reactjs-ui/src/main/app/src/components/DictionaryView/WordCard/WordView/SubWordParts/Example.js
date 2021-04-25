import React from 'react';
import PropTypes from 'prop-types';

import classes from './Example.module.scss';

import { exampleType } from '../wordTypes';
import { Remark } from '.';

const Example = props => {
    const { entry } = props;
    const { remark, expression, explanation } = entry;

    return (
        <div className={classes.ExampleWrapper}>
            {remark && <Remark value={remark} />}
            <span className={classes.Example}>{expression} - {explanation}</span>
        </div>
    );
};

Example.propTypes = {
    entry: exampleType.isRequired,
};

export default Example;
