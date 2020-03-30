# WordsMerger
Join multiple files with multiple words to give result similar to global sorting


HOW TO RUNNING THE APPLICATION?
------------------------------
1. Set the environment variable SOURCE_DIRECTORY=`Location Of directory where files are maintained`
2. Observe the console logs for `OUTPUT FILE PATH`. It will give the location of result file.

HOW TO ACHIEVE HIGHER LEVEL OF PARALLEL PROCESSING?
---------------------------------------------------
1. Set the EXECUTOR_SIZE=`Number of threads to do parallel execution`
2. Set the FILE_MERGE_SIZE=`Number of files to be merged by a single thread`

Note: We need to have the correspoding FileMergerImpl defined in the system before providing FILE_MERGE_SIZE. As of now, its value is 2 since i have done implementation for TwoFileMerger.


WHY FILE_MERGE_SIZE IS NEEDED WHEN WE HAVE EXECUTOR_SIZE DEFINED?
-----------------------------------------------------------------
EXECUTOR_SIZE controls the number of thread that will do the processing and after a certain limit its performance starts
degrading. So, instead of multiple thread we can pick more number of files for merging.
