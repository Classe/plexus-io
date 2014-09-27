package org.codehaus.plexus.components.io.attributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kristian Rosenvold
 */
public class Java7AttributeUtils
{
    /*
    Reads last-modified with proper failure handling if something goes wrong.
     */
    public static long getLastModified( File file )
    {
        try
        {
            BasicFileAttributes basicFileAttributes = Files.readAttributes( file.toPath(), BasicFileAttributes.class );
            return basicFileAttributes.lastModifiedTime().toMillis();
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }

    }

    public static void chmod( File file, int mode )
        throws IOException
    {
        final Path path = file.toPath();
        if (Files.isSymbolicLink( path )){
            if (Files.readSymbolicLink( path ).toFile().exists()){
                Files.setPosixFilePermissions( path, getPermissions( mode ) );
            }
        } else {
            Files.setPosixFilePermissions( path, getPermissions( mode ) );
        }
    }

    public static Set<PosixFilePermission> getPermissions( int mode )
    {
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        //add owners permission
        if ( ( mode & 0400 ) > 0 )
        {
            perms.add( PosixFilePermission.OWNER_READ );
        }
        if ( ( mode & 0200 ) > 0 )
        {
            perms.add( PosixFilePermission.OWNER_WRITE );
        }
        if ( ( mode & 0100 ) > 0 )
        {
            perms.add( PosixFilePermission.OWNER_EXECUTE );
        }
        //add group permissions
        if ( ( mode & 0040 ) > 0 )
        {
            perms.add( PosixFilePermission.GROUP_READ );
        }
        if ( ( mode & 0020 ) > 0 )
        {
            perms.add( PosixFilePermission.GROUP_WRITE );
        }
        if ( ( mode & 0010 ) > 0 )
        {
            perms.add( PosixFilePermission.GROUP_EXECUTE );
        }
        //add others permissions
        if ( ( mode & 0004 ) > 0 )
        {
            perms.add( PosixFilePermission.OTHERS_READ );
        }
        if ( ( mode & 0002 ) > 0 )
        {
            perms.add( PosixFilePermission.OTHERS_WRITE );
        }
        if ( ( mode & 0001 ) > 0 )
        {
            perms.add( PosixFilePermission.OTHERS_EXECUTE );
        }
        return perms;
    }

    public static PosixFileAttributes getPosixFileAttributes( File file )
        throws IOException
    {
        return Files.readAttributes( file.toPath(), PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS );
    }

}
