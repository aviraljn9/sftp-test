package com.mycompany.app;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

/**
 * Hello world!
 *
 */

public class App {
    public static final String FS_DEFAULT_NAME_KEY = CommonConfigurationKeys.FS_DEFAULT_NAME_KEY;
    public static final String DEFAULT_FS = CommonConfigurationKeys.FS_DEFAULT_NAME_DEFAULT;
    private final boolean recursive = true;
    protected static final List<FileSystem> fileSystems = new ArrayList<>();;

    // public Iterator<FileMetadata> listFiles(final FileSystem fs) throws IOException {
    //     return new Iterator<FileMetadata>() {
    //         Path path1 = fs.getWorkingDirectory();
    //         final RemoteIterator<LocatedFileStatus> it = fs.listFiles(path1, recursive);
    //         LocatedFileStatus current = null;

    //         private TailCall<Boolean> hasNextRec() {
    //             try {
    //                 if (current == null) {
    //                     if (!it.hasNext()) {
    //                         return TailCall.done(false);
    //                     }
    //                     current = it.next();
    //                     return this::hasNextRec;
    //                 }
    //                 if (current.isFile() && fileRegexp.matcher(current.getPath().getName()).find()) {
    //                     return TailCall.done(true);
    //                 }
    //                 current = null;
    //                 return this::hasNextRec;
    //             } catch (IOException ioe) {
    //                 throw new ConnectException(ioe);
    //             }
    //         }

            // @Override
            // public boolean hasNext() {
            //     return hasNextRec().invoke();
            // }

            // @Override
            // public FileMetadata next() {
            //     if (!hasNext() && current == null) {
            //         throw new NoSuchElementException("There are no more items.");
            //     }
            //     FileMetadata metadata = toMetadata(current);
            //     current = null;
            //     return metadata;
            // }
    //     };
    // }

    public static URI getDefaultUri(Configuration conf) {
        URI uri = URI.create(fixName(conf.get(FS_DEFAULT_NAME_KEY, DEFAULT_FS)));
        if (uri.getScheme() == null) {
            throw new IllegalArgumentException("No scheme in default FS: " + uri);
        }
        return uri;
    }

    private static String fixName(String name) {
        // convert old-format name to new-format name
        if (name.equals("local")) { // "local" is now "file:///".
            name = "file:///";
        } else if (name.indexOf('/') == -1) { // unqualified is "hdfs://"
            name = "hdfs://" + name;
        }
        return name;
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Configuration config = new Configuration();
        config.set("fs.sftp.impl", "org.apache.hadoop.fs.sftp.SFTPFileSystem");
        config.set("fs.sftp.keyfile", "/Users/aviral/.ssh/id_rsa");
        String my_uri = "sftp://aviral.jain@gurgaon.gravitontrading.com:2222/myhome";
        // String my_uri = "file:///Users/aviral";
        Path workingDir = new Path(my_uri);
        URI uri = workingDir.toUri();

        try {
            FileSystem fs2 = FileSystem.newInstance(uri, config);
            fs2.setWorkingDirectory(workingDir);
            fileSystems.add(fs2);
            String newuri = Optional.ofNullable(fs2.getWorkingDirectory()).orElse(new Path("./")).toString();
            List<String> uris = new ArrayList<>();
            Path path1 = fs2.getWorkingDirectory();
            final RemoteIterator<LocatedFileStatus> it = fs2.listFiles(path1, false);

            int x=1;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
