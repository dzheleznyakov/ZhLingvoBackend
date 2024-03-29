import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';

import classes from './RecordList.module.scss';

import { useActionOnMount } from '../../../hooks';
import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';
import RecordListItem from './RecordListItem/RecordListItem';
import RecordListControl from './RecordListControl/RecordListControl';
import { ListView } from '../../UI';

const RecordList = () => {
    const { qid: quizId, rid } = useParams();
    const dispatch = useDispatch();
    const overviews = useSelector(selectors.quizRecordsOverviewsSelector);
    const isEditing = useSelector(selectors.quizRecordIsEditingSelector);
    const displayedOverviews = [...overviews];
    displayedOverviews.sort((a, b) => {
        const scoreDiff = a.currentScore - b.currentScore;
        if (scoreDiff !== 0)
            return scoreDiff;
        return a.id - b.id;
    });

    const selectedIndex = rid !== null && rid !== undefined
        ? displayedOverviews.findIndex(overview => overview.id === +rid)
        : -1;

    useActionOnMount(actions.fetchQuizRecordsOverviews(quizId));

    useEffect(() => {
        if (rid !== null && rid !== undefined) {
            const selectedQuizRecordIndex = overviews.findIndex(qRec => qRec.id === +rid);
            dispatch(actions.selectQuizRecord(selectedQuizRecordIndex));
        }
    }, []); // eslint-disable-line react-hooks/exhaustive-deps

    const wrapperClasses = [classes.QuizRecordListWrapper];
    if (rid !== null && rid !== undefined)
        wrapperClasses.push(classes.Active);

    const items = displayedOverviews
        .map(overview => 
            ({ 
                key: `${overview.id}`, 
                node: <RecordListItem quizRecord={overview} /> ,
                currentScore: overview.currentScore,
            }));

    const onRecordClick = index => () => {
        dispatch(actions.selectQuizRecord(index));
        const recordId = displayedOverviews[index].id;
        dispatch(actions.navigateTo(`/tutor/quiz/${quizId}/${recordId}`));
    };

    const listView = items.length === 0
        ? null
        : <ListView
            items={items}
            onItemClick={onRecordClick} 
            selectedIndex={selectedIndex}
            width={200} />;
    
    return (
        <div className={wrapperClasses.join(' ')}>
            {listView}
            <RecordListControl />
            {isEditing && <div className={classes.Overlay} />}
        </div>
    );
};

export default RecordList;
