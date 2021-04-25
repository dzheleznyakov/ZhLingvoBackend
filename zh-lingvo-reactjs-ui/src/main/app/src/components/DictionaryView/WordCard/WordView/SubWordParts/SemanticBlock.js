import React from 'react';
import PropTypes from 'prop-types';

import classes from './SemanticBlock.module.scss';

import Meaning from './Meaning';
import { semBlockType } from '../wordTypes';

const SemanticBlock = props => {
    const { semBlock, index, path } = props;
    const { pos } = semBlock;

    const meanings = (semBlock.meanings || [])
        .map((m, i) => <Meaning path={[...path, 'meanings', `${i}`]} key={m.id} meaning={m} />)

    return (
        <div>
            <div>
                <span className={classes.SemBlockEnum}>{index + 1}. </span>
                <span className={classes.PoS}>{pos}</span>
            </div>
            <div>
                <ol className={classes.MeaningsList}>
                    {meanings}
                </ol>
            </div>
        </div>
    );
};

SemanticBlock.propTypes = {
    index: PropTypes.number.isRequired,
    semBlock: semBlockType.isRequired,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default SemanticBlock;
