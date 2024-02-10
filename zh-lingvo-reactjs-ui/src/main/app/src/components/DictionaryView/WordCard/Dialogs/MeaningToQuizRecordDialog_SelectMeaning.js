import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classes from './MeaningToQuizRecordDialog.module.scss';

import { Dialog, Excerpt, IconButton, Table, buttonSizes, iconButtonTypes } from '../../../UI';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';
import { Meaning } from '../WordView/SubWordParts';
import { MEANING_TO_QUIZ_RECORD__CHOOSE_QUIZ } from '../../../../static/constants/wordEditModalTypes';

const MeaningToQuizRecordDialog_SelectMeaning = () => {
    const dispatch = useDispatch();
    const word = useSelector(selectors.loadedWordSelector) || [];
    const meanings = word
        .map((entry, index) => entry.semBlocks.map((sb, sbIndex) => ({
            ...sb,
            path: [`${index}`, 'semBlocks', `${sbIndex}`],
        })))
        .reduce((flatArray, arr) => flatArray.concat(arr), [])
        .map(item => item.meanings.map((m, mIndex) =>({
            item: m,
            path: [...item.path, 'meanings', `${mIndex}`],
        })))
        .reduce((flatArray, arr) => flatArray.concat(arr), []);

        const columnsDef = [
            { name: '', label: 'meaning' },
            { name: '', label: 'action' },
        ];

        const data = meanings.map(m => ({
            meaning: { value: (
                <Excerpt>
                    <Meaning
                        meaning={m.item}
                        path={m.path}
                        editable={false}
                    />
                </Excerpt>
            )},
            action: { value: (
                <IconButton
                    type={iconButtonTypes.REDIRECT}
                    size={buttonSizes.MEDIUM}
                    clicked={() => {
                        dispatch(actions.storeMeaningToConvertToQuizRecord(m.item));
                        dispatch(actions.fetchWordSuccess(word));
                        dispatch(actions.setWordEditModalType(
                            MEANING_TO_QUIZ_RECORD__CHOOSE_QUIZ, 
                            m.path,
                        ));
                    }}
                />
            ) },
        }));

    return (
        <Dialog className={classes.MeaningWrapper}
            close={() => 
                dispatch(actions.shouldShowWordEditModal(false))
            }
        >
                <Table 
                    columnsDef={columnsDef}
                    data={data}
                />
        </Dialog>
    )};

export default MeaningToQuizRecordDialog_SelectMeaning;
